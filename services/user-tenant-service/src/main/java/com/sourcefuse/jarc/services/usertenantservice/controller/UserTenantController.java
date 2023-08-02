package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user-tenants")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserTenantController {

  private final UserTenantService userTenantService;

  @GetMapping("{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.VIEW_ANY_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER_RESTRICTED +
    "','" +
    PermissionKeyConstants.VIEW_OWN_USER +
    "')"
  )
  public ResponseEntity<UserView> getUserTenantById(
    @PathVariable("id") UUID id
  ) {
    /*** INFO :As discussed by samarpan currently
     checkViewTenantRestrictedPermissions we
     dont have to implement..
     One tenant cannot see others tenant data ***/
    return new ResponseEntity<>(
      userTenantService.getUserTenantById(id),
      HttpStatus.OK
    );
  }
}
