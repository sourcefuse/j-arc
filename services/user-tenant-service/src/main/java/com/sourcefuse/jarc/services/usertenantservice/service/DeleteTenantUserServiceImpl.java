package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserGroupsSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantSpecification;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class DeleteTenantUserServiceImpl implements DeleteTenantUserService {

  private final UserRepository userRepository;
  private final UserTenantRepository userTenantRepository;
  private final UserGroupsRepository userGroupsRepository;

  @Override
  public void deleteUserById(CurrentUser currentUser, UUID id, UUID tenantId) {
    checkForDeleteTenantUserPermission(currentUser, id);
    checkForDeleteAnyUserPermission(currentUser, tenantId);

    Optional<UserTenant> existingUserTenant = userTenantRepository.findOne(
      UserTenantSpecification.byUserIdAndTenantIdOrderByIdAsc(
        id,
        currentUser.getTenantId()
      )
    );

    if (existingUserTenant.isPresent()) {
      userGroupsRepository.delete(
        UserGroupsSpecification.byUserTenantId(existingUserTenant.get().getId())
      );
    }
    userTenantRepository.delete(
      UserTenantSpecification.byUserIdAndTenantId(id, currentUser.getTenantId())
    );

    UserTenant userTenant = userTenantRepository
      .findAll(UserTenantSpecification.byUserId(id))
      .stream()
      .findFirst()
      .orElse(null);
    UUID defaultTenantId = null;
    if (userTenant != null) {
      defaultTenantId = userTenant.getTenant().getId();
    }

    Optional<User> savedUser = userRepository.findById(id);
    User user;
    if (savedUser.isPresent()) {
      user = savedUser.get();
      if (defaultTenantId != null) {
        user.getDefaultTenant().setId(defaultTenantId);
      }
      userRepository.save(user);
    }
  }

  private void checkForDeleteTenantUserPermission(
    CurrentUser currentUser,
    UUID id
  ) {
    /** INFO ::delete tenant
     * user permission check removed**/
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.DELETE_TENANT_USER.toString())
    ) {
      Optional<UserTenant> userTenant = userTenantRepository.findOne(
        UserTenantSpecification.byUserIdAndTenantIdOrderByIdAsc(
          id,
          currentUser.getTenantId()
        )
      );
      if (!userTenant.isPresent()) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
        );
      }
    }
  }

  private static void checkForDeleteAnyUserPermission(
    CurrentUser currentUser,
    UUID tenantId
  ) {
    if (
      !currentUser
        .getPermissions()
        .contains(PermissionKey.DELETE_ANY_USER.toString()) &&
      !tenantId.equals(currentUser.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }
}
