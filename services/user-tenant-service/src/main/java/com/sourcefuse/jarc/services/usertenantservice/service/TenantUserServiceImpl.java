package com.sourcefuse.jarc.services.usertenantservice.service;

import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.RoleTypeMap;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commons.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.*;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import com.sourcefuse.jarc.services.usertenantservice.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TenantUserServiceImpl implements TenantUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserTenantRepository userTenantRepository;
  private final UserViewRepository userViewRepository;
  private final AuthClientsRepository authClientsRepository;
  private final UserGroupsRepository userGroupsRepository;

  @PersistenceContext
  private EntityManager em;

  public UserDto create(
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Object options
  ) {
    Map mapOption = new HashMap();
    User user = userData.getUserDetails();
    if (options != null) {
      mapOption = (Map) options;
    }
    validateUserCreation(user, userData, currentUser, mapOption);
    String roleType;
    Optional<Role> optRole = roleRepository.findById(userData.getRoleId());
    Role role;
    if (optRole.isPresent()) {
      role = optRole.get();
      roleType = RoleTypeMap.get(role.getRoleType()).permissionKey();
    } else throw new ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "${no.role.is.present}"
    );

    User existingUser = userRepository.findByUsernameOrEmail(
      user.getUsername(),
      user.getEmail()
    );
    if (existingUser != null) {
      Optional<UserTenant> existingUserTenant =
        userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
          existingUser.getId(),
          userData.getTenantId()
        );
      if (existingUserTenant.isPresent()) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "${user.exits}"
        );
      } else {
        if (
          currentUser.getTenantId() == userData.getTenantId() &&
          currentUser
            .getPermissions()
            .contains(PermissionKey.CREATE_TENANT_USER_RESTRICTED.toString()) &&
          !currentUser.getPermissions().contains("CreateTenant" + roleType)
        ) {
          throw new ResponseStatusException(
            HttpStatus.FORBIDDEN,
            AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
          );
        }
        UserTenant userTenant = createUserTenantData(
          userData,
          UserStatus.REGISTERED,
          existingUser.getId(),
          mapOption
        );
        return new UserDto(
          existingUser,
          userTenant.getRoleId(),
          userTenant.getStatus(),
          userTenant.getTenantId(),
          userTenant.getId(),
          String.valueOf(
            mapOption.get("authProvider") != null
              ? mapOption.get("authProvider")
              : ""
          )
        );
      }
    }

    if (
      currentUser.getTenantId() == userData.getTenantId() &&
      currentUser
        .getPermissions()
        .contains(PermissionKey.CREATE_TENANT_USER_RESTRICTED.toString()) &&
      !currentUser.getPermissions().contains("CreateTenant" + roleType)
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    //todo doubt :: pending dynamic query
    List<AuthClient> authClients = authClientsRepository.findByClientIdIn(
      role.getAllowedClients(),
      Role.class
    );
    //List<AuthClient> authClients=Arrays.asList(new AuthClient[]{new AuthClient(UUID.randomUUID()),
    //      new AuthClient(UUID.randomUUID())});
    List<String> authClientIds = authClients
      .stream()
      .map(auth -> String.valueOf(auth.getId()))
      .collect(Collectors.toList());
    user.setAuthClientIds("{" + StringUtils.join(authClientIds, ',') + "}");

    user.setUsername(user.getUsername().toLowerCase());
    user.setDefaultTenantId(userData.getTenantId());
    User savedUser = userRepository.save(user);
    UserTenant userTenant = createUserTenantData(
      userData,
      UserStatus.REGISTERED,
      savedUser.getId(),
      mapOption
    );
    return new UserDto(
      savedUser,
      userTenant.getRoleId(),
      userTenant.getStatus(),
      userTenant.getTenantId(),
      userTenant.getId(),
      String.valueOf(
        mapOption.get("authProvider") != null
          ? mapOption.get("authProvider")
          : ""
      )
    );
  }

  @Override
  public Map<String, Object> checkViewTenantRestrictedPermissions(
    IAuthUserWithPermissions currentUser,
    Predicate predicate,
    Class cls
  ) {
    Map<String, Object> map = new HashMap<>();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery cq = cb.createQuery();
    Root<UserView> root = cq.from(cls);
    Predicate finl = null;

    List<Role> roleLis = roleRepository.findAll();
    List<UUID> allowedRoles = new ArrayList<>();
    for (Role r : roleLis) {
      if (
        r.getId() != null &&
        currentUser
          .getPermissions()
          .contains(
            "ViewTenant" + RoleTypeMap.get(r.getRoleType()).permissionKey()
          )
      ) {
        allowedRoles.add(r.getId());
      }
    }

    if (predicate != null) {
      Predicate inPre = root.get("roleId").in(allowedRoles);
      finl = cb.and(inPre, predicate);
    } else {
      finl = root.get("roleId").in(allowedRoles);
    }
    map.put(CommonConstants.PREDICATE, finl);
    map.put(CommonConstants.CRITERIA_QUERY, cq);
    map.put(CommonConstants.BUILDER, cb);
    return map;
  }

  @Override
  public List<UserView> getUserView(CriteriaQuery cq) {
    return userViewRepository.findUserView(cq);
  }

  @Override
  public List<UserView> getAllUsers(UUID id, Class type) {
    return userViewRepository.getAllUserView(id, type);
  }

  @Override
  public List<UserView> count(CriteriaQuery cq) {
    return userViewRepository.countUser(cq);
  }

  @Override
  public UserView findById(UUID userId, UUID id, Class type) {
    return (UserView) userViewRepository.findById(userId, id, type);
  }

  @Override
  public void updateById(
    IAuthUserWithPermissions currentUser,
    UUID id,
    UserView userView,
    UUID tenantId
  ) {
    checkForUpdatePermissions(currentUser, id, tenantId);
    if (userView.getUsername() != null) {
      long usrCount = userRepository.countByIdNotAndUsername(
        id,
        userView.getUsername()
      );
      if (usrCount > 0) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "${user.name.exits}"
        );
      }
    }
    User tempUser = new User();
    BeanUtils.copyProperties(userView, tempUser);
    User savUser;

    if (tempUser != null) {
      Optional<User> optUser = userRepository.findById(id);
      if (optUser.isPresent()) {
        savUser = optUser.get();
        BeanUtils.copyProperties(
          tempUser,
          savUser,
          CommonUtils.getNullPropertyNames(tempUser)
        );
        savUser = userRepository.save(savUser);
      }
      updateUserTenant(userView, id, currentUser);
    }
  }

  @Override
  public void deleteUserById(
    IAuthUserWithPermissions currentUser,
    UUID id,
    UUID tenantId
  ) {
    checkForDeleteTenantUserRestrictedPermission(currentUser, id);
    checkForDeleteTenantUserPermission(currentUser, id);
    checkForDeleteAnyUserPermission(currentUser, tenantId);

    Optional<UserTenant> existingUserTenant =
      userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
        id,
        currentUser.getTenantId()
      );

    if (existingUserTenant.isPresent()) {
      userGroupsRepository.deleteAllByUserTenantId(
        existingUserTenant.get().getId()
      );
    }
    userTenantRepository.deleteAllByUserIdAndTenantId(
      id,
      currentUser.getTenantId()
    );

    UserTenant userTenant = userTenantRepository.findByUserId(id);
    UUID defaultTenantId = null;
    if (userTenant != null) {
      defaultTenantId = userTenant.getTenantId();
    }

    Optional<User> optUser = userRepository.findById(id);
    User user;
    if (optUser.isPresent()) {
      user = optUser.get();
      user.setDefaultTenantId(defaultTenantId);
      user = userRepository.save(user);
    }
  }

  private void checkForDeleteAnyUserPermission(
    IAuthUserWithPermissions currentUser,
    UUID tenantId
  ) {
    if (
      !currentUser
        .getPermissions()
        .contains(PermissionKey.DELETE_ANY_USER.toString()) &&
      tenantId != currentUser.getTenantId()
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  private void checkForDeleteTenantUserPermission(
    IAuthUserWithPermissions currentUser,
    UUID id
  ) {
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.DELETE_TENANT_USER.toString())
    ) {
      Optional<UserTenant> userTenant =
        userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
          id,
          currentUser.getTenantId()
        );
      if (!userTenant.isPresent()) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  private void checkForDeleteTenantUserRestrictedPermission(
    IAuthUserWithPermissions currentUser,
    UUID id
  ) {
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.DELETE_TENANT_USER_RESTRICTED.toString())
    ) {
      Optional<UserTenant> userTenant =
        userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
          id,
          currentUser.getTenantId()
        );
      //  userTenant.getRole().getRoleType() doubt::
      //RoleType.DEFAULT need to change
      if (
        !userTenant.isPresent() ||
        !currentUser
          .getPermissions()
          .contains(
            "DeleteTenant" + RoleTypeMap.get(RoleType.DEFAULT).permissionKey()
          )
      ) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
        );
      }
    }
  }

  private void updateUserTenant(
    UserView userView,
    UUID id,
    IAuthUserWithPermissions currentUser
  ) {
    UserTenant utData = new UserTenant();
    if (userView.getRoleId() != null) {
      utData.setRoleId(userView.getRoleId());
    }
    if (userView.getStatus() != null) {
      utData.setStatus(userView.getStatus());
    }
    if (utData.getRoleId() != null && utData.getStatus() != null) {
      List<UserTenant> usrTntLis =
        userTenantRepository.findAllByUserIdAndTenantId(
          id,
          userView.getTenantId() != null
            ? userView.getTenantId()
            : currentUser.getTenantId()
        );
      usrTntLis.forEach(usrTnt -> {
        BeanUtils.copyProperties(
          utData,
          usrTnt,
          CommonUtils.getNullPropertyNames(utData)
        );
        usrTnt = userTenantRepository.save(usrTnt);
      });
    }
  }

  private void checkForUpdatePermissions(
    IAuthUserWithPermissions currentUser,
    UUID id,
    UUID tenantId
  ) {
    if (
      currentUser.getId() != id &&
      currentUser
        .getPermissions()
        .contains(PermissionKey.UPDATE_OWN_USER.toString())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    if (
      currentUser.getTenantId() == tenantId &&
      currentUser
        .getPermissions()
        .contains(PermissionKey.UPDATE_TENANT_USER_RESTRICTED.toString()) &&
      currentUser.getId() != id
    ) {
      Optional<UserTenant> userTenant =
        userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
          id,
          currentUser.getTenantId()
        );
      //  userTenant.getRole().getRoleType()  doubt::
      //RoleType.DEFAULT this need to change
      if (
        !userTenant.isPresent() ||
        !currentUser
          .getPermissions()
          .contains(
            "UpdateTenant" + RoleTypeMap.get(RoleType.DEFAULT).permissionKey()
          )
      ) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
        );
      }
    }

    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.UPDATE_TENANT_USER.toString())
    ) {
      Optional<UserTenant> userTenant =
        userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
          id,
          currentUser.getTenantId()
        );

      if (!userTenant.isPresent()) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
        );
      }
    }

    if (
      !currentUser
        .getPermissions()
        .contains(PermissionKey.UPDATE_ANY_USER.toString()) &&
      !tenantId.equals(currentUser.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  private void validateUserCreation(
    User user,
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Map options
  ) {
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.CREATE_TENANT_USER.getKey()) &&
      !currentUser.getTenantId().equals(userData.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }

    if (user != null && user.getEmail() != null) user.setEmail(
      user.getEmail().toLowerCase().trim()
    );

    // Check for valid email
    String emailRegex = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    if (
      user != null &&
      user.getEmail() != null &&
      !user.getEmail().matches(emailRegex)
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "${email.invalid}"
      );
    }

    // Check for allowed domains
    String[] allowedDomains = (System.getenv("AUTO_SIGNUP_DOMAINS") != null)
      ? System.getenv("AUTO_SIGNUP_DOMAINS").split(",")
      : new String[0];
    String[] email = user.getEmail().split("@");
    if (
      email.length != 2 ||
      allowedDomains.length == 0 ||
      !Arrays.asList(allowedDomains).contains(email[1])
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "${domain.not.support}"
      );
    }

    if (
      allowedDomains != null &&
      allowedDomains.length == 1 &&
      allowedDomains[0].equals("*") &&
      options != null
    ) {
      options.put("authProvider", "keycloak");
      // options.setAuthProvider("keycloak");
    } else if (options != null) {
      options.put(
        "authProvider",
        options.get("authProvider") != null &&
          Arrays.asList(allowedDomains).contains(email[1])
          ? options.get("authProvider")
          : "internal"
      );
    }
    // Implement user creation validation logic here
  }

  private UserTenant createUserTenantData(
    UserDto userData,
    UserStatus status,
    UUID userId,
    Map options
  ) {
    UserTenant userTenant = UserTenant
      .builder()
      .roleId(userData.getRoleId())
      .tenantId(userData.getTenantId())
      .userId(userId)
      .status(status)
      .build();

    userTenant = userTenantRepository.save(userTenant);
    return userTenant;
    // Implement user tenant creation logic here
  }
}
