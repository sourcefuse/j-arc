package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserViewSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserTenantServiceImpl implements UserTenantService {

  private final RoleUserTenantRepository roleUserTenantRepository;

  private final UserViewRepository userViewRepository;

  @Override
  public UserView getUserTenantById(UUID id) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    UserTenant userTenant = roleUserTenantRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No User-Tenant is present against given value" + id
        )
      );
    CurrentUserUtils.compareWithCurrentUserTenantId(
      userTenant.getTenant().getId(),
      currentUser
    );

    CurrentUserUtils.compareWithCurrentUsersUserId(
      userTenant.getUser().getId(),
      currentUser
    );

    /*** INFO :As discussed by samarpan currently
     checkViewTenantRestrictedPermissions we
     dont have to implement***/
    return userViewRepository
      .findOne(UserViewSpecification.byUserTenantId(id))
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found")
      );
  }
}
