package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/user-tenants")
@RequiredArgsConstructor
public class UserTenantController {

  private final RoleUserTenantRepository roleUserTenantRepository;

  private final UserViewRepository userViewRepository;

  @GetMapping("{id}")
  public ResponseEntity<Object> getUserTenantById(@PathVariable("id") UUID id) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    UserTenant userTenant = roleUserTenantRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No User-Tenant is present against given value" + id
        )
      );
    if (
      !currentUser.getTenantId().equals(userTenant.getTenant().getId()) &&
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_ANY_USER.getValue())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.getValue()
      );
    }
    if (
      !currentUser.getId().equals(userTenant.getUser().getId()) &&
      currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_USER.getValue())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.getValue()
      );
    }

    UserView userView = userViewRepository.findByUserTenantId(id);
    if (userView == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found");
    }
    return new ResponseEntity<>(userView, HttpStatus.OK);
  }
}
