package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpdateTenantUserServiceImpl implements UpdateTenantUserService {

  private final UserTenantRepository userTenantRepository;

  private final UserRepository userRepository;

  @Value("$user.name.exits")
  String userNameExits;

  @Override
  public void updateById(
    CurrentUser currentUser,
    UUID id,
    UserView userView,
    UUID tenantId
  ) {
    checkForUpdatePermissions(currentUser, id, tenantId);
    if (userView.getUsername() != null) {
      long userCount = userRepository.count(
        UserSpecification.byIdNotAndUsername(id, userView.getUsername())
      );
      if (userCount > 0) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, userNameExits);
      }
    }
    /***INFO :Removed type safety
     * as discussed with harshad*/
    User savedUser;
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      savedUser = user.get();
      BeanUtils.copyProperties(
        userView,
        savedUser,
        CommonUtils.getNullPropertyNames(userView)
      );
      userRepository.save(savedUser);
    }
    updateUserTenant(userView, id, currentUser);
  }

  private void checkForUpdatePermissions(
    CurrentUser currentUser,
    UUID id,
    UUID tenantId
  ) {
    checkForUserId(currentUser, id);

    /*** INFO: :if condition to check
     * UPDATE_TENANT_USER removed***/
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
    CurrentUserUtils.compareWithCurrentUserTenantId(tenantId, currentUser);
  }

  private static void checkForUserId(CurrentUser currentUser, UUID id) {
    CurrentUserUtils.compareWithCurrentUsersUserId(id, currentUser);
    //Restricted permission removed.
  }

  private void updateUserTenant(
    UserView userView,
    UUID id,
    CurrentUser currentUser
  ) {
    UserTenant userTenantData = new UserTenant();
    if (userView.getRoleId() != null) {
      userTenantData.setRole(new Role());
      userTenantData.getRole().setId(userView.getRoleId());
    }
    if (userView.getStatus() != null) {
      userTenantData.setStatus(userView.getStatus());
    }
    if (userTenantData != null) {
      List<UserTenant> userTenantList = userTenantRepository.findAll(
        UserTenantSpecification.byUserIdAndTenantId(
          id,
          userView.getTenantId() != null
            ? userView.getTenantId()
            : currentUser.getTenantId()
        )
      );
      userTenantList.forEach((UserTenant userTenant) -> {
        BeanUtils.copyProperties(
          userTenantData,
          userTenant,
          CommonUtils.getNullPropertyNames(userTenantData)
        );
        userTenantRepository.save(userTenant);
      });
      log.info("User Tenant successfully updated ................");
    }
  }
}
