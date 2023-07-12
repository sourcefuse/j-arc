package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.AuthClient;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.repository.AuthClientsRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.AuthClientsSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserViewSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${user.exits}")
  String userExits;

  @Value("${no.role.is.present}")
  String noRolePresent;

  public UserDto create(
    UserDto userData,
    CurrentUser currentUser,
    Map<String, String> options
  ) {
    Map<String, String> mapOption = new HashMap<>();

    User user = userData.getUserDetails();
    if (options != null) {
      mapOption = options;
    }
    validateUserCreation(user, userData, currentUser, mapOption);
    Optional<Role> savedRole = roleRepository.findById(userData.getRoleId());
    Role role;
    if (savedRole.isPresent()) {
      role = savedRole.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, noRolePresent);
    }

    UserDto existingUser = getUserDto(userData, mapOption, user);
    if (existingUser != null) {
      return existingUser;
    }
    /*** INFO:Restricted permission check removed*/
    List<AuthClient> authClients = authClientsRepository.findAll(
      AuthClientsSpecification.byClientIdIn(role.getAllowedClients())
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
    user.getDefaultTenant().setId(userData.getTenantId());
    User savedUser = userRepository.save(user);
    UserTenant userTenant = createUserTenantData(
      userData,
      UserStatus.REGISTERED,
      savedUser.getId()
    );
    return new UserDto(
      savedUser,
      userTenant.getRole().getId(),
      userTenant.getStatus(),
      userTenant.getTenant().getId(),
      userTenant.getId(),
      String.valueOf(
        mapOption.get(CommonConstants.AUTH_PROVIDER) != null
          ? mapOption.get(CommonConstants.AUTH_PROVIDER)
          : ""
      )
    );
  }

  private UserDto getUserDto(
    UserDto userData,
    Map<String, String> mapOption,
    User user
  ) {
    Optional<User> existingUser = userRepository.findOne(
      UserSpecification.byUserNameOrEmail(user.getUsername(), user.getEmail())
    );
    if (existingUser.isPresent()) {
      Optional<UserTenant> existingUserTenant = userTenantRepository.findOne(
        UserTenantSpecification.byUserIdAndTenantIdOrderByIdAsc(
          existingUser.get().getId(),
          userData.getTenantId()
        )
      );
      if (existingUserTenant.isPresent()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userExits);
      } else {
        /*** INFO: Restricted permission check removed */
        log.info("User exist but not registered");
        UserTenant userTenant = createUserTenantData(
          userData,
          UserStatus.REGISTERED,
          existingUser.get().getId()
        );
        return new UserDto(
          existingUser.get(),
          userTenant.getRole().getId(),
          userTenant.getStatus(),
          userTenant.getTenant().getId(),
          userTenant.getId(),
          String.valueOf(
            mapOption.get(CommonConstants.AUTH_PROVIDER) != null
              ? mapOption.get(CommonConstants.AUTH_PROVIDER)
              : ""
          )
        );
      }
    }
    log.info("User is not registered ,processing for registration");
    return null;
  }

  @Override
  public List<UserDto> getUserView(UUID id, CurrentUser currentUser) {
    CurrentUserUtils.checkForViewAnyUserPermission(currentUser, id);
    List<UserView> userViewsList = userViewRepository.findAll(
      UserViewSpecification.byTenantId(id)
    );
    return getUserDtoList(userViewsList);
  }

  private static List<UserDto> getUserDtoList(List<UserView> userViewsList) {
    List<UserDto> userDtoList = new ArrayList<>();
    User user;
    UserDto userDto;
    for (UserView userView : userViewsList) {
      user = new User();
      userDto = new UserDto();
      BeanUtils.copyProperties(userView, userDto);
      BeanUtils.copyProperties(userView, user);
      user.setDefaultTenant(new Tenant(userView.getDefaultTenantId()));
      userDto.setUserDetails(user);
      userDtoList.add(userDto);
    }
    return userDtoList;
  }

  @Override
  public List<UserDto> getAllUsers(UUID tenantId) {
    List<UserView> userViewList = userViewRepository.findAll(
      UserViewSpecification.byTenantId(tenantId)
    );

    return getUserDtoList(userViewList);
  }

  @Override
  public UserView findById(
    UUID userId,
    UUID tenantId,
    CurrentUser currentUser
  ) {
    CurrentUserUtils.checkForViewAnyUserPermission(currentUser, tenantId);

    CurrentUserUtils.checkForViewOwnUserPermission(currentUser, userId);
    return userViewRepository
      .findOne(UserViewSpecification.byUserIdAndTenantId(userId, tenantId))
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No User view is present against given value"
        )
      );
  }

  private static void validateUserCreation(
    User user,
    UserDto userData,
    CurrentUser currentUser,
    Map<String, String> options
  ) {
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.CREATE_TENANT_USER.toString()) &&
      !currentUser.getTenantId().equals(userData.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    // Check for allowed domains
    String[] allowedDomains = (System.getenv("AUTO_SIGNUP_DOMAINS") != null)
      ? System.getenv("AUTO_SIGNUP_DOMAINS").split(",")
      : new String[0];
    String[] email = user.getEmail().split("@");

    assignAuthProvider(options, allowedDomains, email);
  }

  private static void assignAuthProvider(
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
      log.info("Cannot configured Auth_PROVIDER");
    }
  }

  private UserTenant createUserTenantData(
    UserDto userData,
    UserStatus status,
    UUID userId
  ) {
    UserTenant userTenant = UserTenant
      .builder()
      .role(new Role(userData.getRoleId()))
      .tenant(new Tenant(userData.getTenantId()))
      .user(new User(userId))
      .status(status)
      .build();

    return userTenantRepository.save(userTenant);
  }
}
