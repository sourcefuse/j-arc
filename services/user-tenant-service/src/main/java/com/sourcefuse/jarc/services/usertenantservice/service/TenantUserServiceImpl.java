package com.sourcefuse.jarc.services.usertenantservice.service;

import static com.sourcefuse.jarc.services.usertenantservice.commons.TypeRole.roleTypeMap;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commons.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.dto.AuthClient;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import com.sourcefuse.jarc.services.usertenantservice.repository.AuthClientsRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
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

  @PersistenceContext
  private EntityManager em;

  public UserDto create(
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Object options
  ) {
    Map<String, String> mapOption = new HashMap<>();
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
      roleType = roleTypeMap.get(role.getRoleType()).permissionKey();
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "${no.role.is.present}"
      );
    }
    UserDto existingUser = getUserDto(
      userData,
      currentUser,
      mapOption,
      user,
      roleType
    );
    if (existingUser != null) {
      return existingUser;
    }

    extracted(userData, currentUser, roleType);

    List<AuthClient> authClients = authClientsRepository.findByClientIdIn(
      role.getAllowedClients(),
      AuthClient.class
    );

    return getUserDto(userData, mapOption, user, authClients);
  }

  private UserDto getUserDto(
    UserDto userData,
    Map<String, String> mapOption,
    User user,
    List<AuthClient> authClients
  ) {
    List<String> authClientIds = authClients
      .stream()
      .map(auth -> String.valueOf(auth.getId()))
      .toList();
    user.setAuthClientIds("{" + StringUtils.join(authClientIds, ',') + "}");

    user.setUsername(user.getUsername().toLowerCase(Locale.getDefault()));
    user.setDefaultTenantId(userData.getTenantId());
    User savedUser = userRepository.save(user);
    UserTenant userTenant = createUserTenantData(
      userData,
      UserStatus.REGISTERED,
      savedUser.getId()
    );
    return new UserDto(
      savedUser,
      userTenant.getRoleId(),
      userTenant.getStatus(),
      userTenant.getTenantId(),
      userTenant.getId(),
      String.valueOf(
        mapOption.get(CommonConstants.AUTH_PROVIDER) != null
          ? mapOption.get(CommonConstants.AUTH_PROVIDER)
          : ""
      )
    );
  }

  private static void extracted(
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    String roleType
  ) {
    if (
      currentUser.getTenantId().equals(userData.getTenantId()) &&
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
  }

  private UserDto getUserDto(
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Map<String, String> mapOption,
    User user,
    String roleType
  ) {
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
        extracted(userData, currentUser, roleType);
        UserTenant userTenant = createUserTenantData(
          userData,
          UserStatus.REGISTERED,
          existingUser.getId()
        );
        return new UserDto(
          existingUser,
          userTenant.getRoleId(),
          userTenant.getStatus(),
          userTenant.getTenantId(),
          userTenant.getId(),
          String.valueOf(
            mapOption.get(CommonConstants.AUTH_PROVIDER) != null
              ? mapOption.get(CommonConstants.AUTH_PROVIDER)
              : ""
          )
        );
      }
    }
    return null;
  }

  @Override
  public Map<String, Object> checkViewTenantRestrictedPermissions(
    IAuthUserWithPermissions currentUser,
    Predicate predicate,
    Class cls
  ) {
    Map<String, Object> map = new HashMap<>();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<UserView> cq = cb.createQuery(UserView.class);
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
            "ViewTenant" + roleTypeMap.get(r.getRoleType()).permissionKey()
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
    return userViewRepository.findById(userId, id, type);
  }

  private static void validateUserCreation(
    User user,
    UserDto userData,
    IAuthUserWithPermissions currentUser,
    Map<String, String> options
  ) {
    extracted(userData, currentUser);

    extracted(user);

    // Check for allowed domains
    String[] allowedDomains = (System.getenv("AUTO_SIGNUP_DOMAINS") != null)
      ? System.getenv("AUTO_SIGNUP_DOMAINS").split(",")
      : new String[0];
    String[] email = user.getEmail().split("@");
    if (
      email.length != CommonConstants.TWO ||
      allowedDomains.length == 0 ||
      !Arrays.asList(allowedDomains).contains(email[1])
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "${domain.not.support}"
      );
    }

    extracted(options, allowedDomains, email);
    // Implement user creation validation logic here
  }

  private static void extracted(User user) {
    if (user.getEmail() != null) {
      user.setEmail(user.getEmail().toLowerCase(Locale.getDefault()).trim());
    }
    // Check for valid email
    String emailRegex = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    if (user.getEmail() != null && !user.getEmail().matches(emailRegex)) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "${email.invalid}"
      );
    }
  }

  private static void extracted(
    Map<String, String> options,
    String[] allowedDomains,
    String[] email
  ) {
    if (
      allowedDomains.length == 1 &&
      "*".equals(allowedDomains[0]) &&
      options != null
    ) {
      options.put(CommonConstants.AUTH_PROVIDER, "keycloak");
    } else if (options != null) {
      options.put(
        CommonConstants.AUTH_PROVIDER,
        options.get(CommonConstants.AUTH_PROVIDER) != null &&
          Arrays.asList(allowedDomains).contains(email[1])
          ? options.get(CommonConstants.AUTH_PROVIDER)
          : "internal"
      );
    } else {
      System.out.println("Cannot configured Auth_PROVIDER");
    }
  }

  private static void extracted(
    UserDto userData,
    IAuthUserWithPermissions currentUser
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
  }

  private UserTenant createUserTenantData(
    UserDto userData,
    UserStatus status,
    UUID userId
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
