package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import java.util.Optional;
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

  private final RoleUserTenantRepository roleUserTRepository;

  private final UserViewRepository userViewRepository;

  // pending authentication doubt ::
  @GetMapping("{id}")
  public ResponseEntity<Object> getUserTenantById(@PathVariable("id") UUID id) {
    Optional<UserTenant> userTenant = roleUserTRepository.findById(id);
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    if (userTenant.isPresent()) {
      if (
        !currentUser
          .getTenantId()
          .equals(userTenant.get().getTenant().getId()) &&
        !currentUser
          .getPermissions()
          .contains(PermissionKey.VIEW_ANY_USER.toString())
      ) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
        );
      }
      if (
        !currentUser.getId().equals(userTenant.get().getUser().getId()) &&
        currentUser
          .getPermissions()
          .contains(PermissionKey.VIEW_OWN_USER.toString())
      ) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
        );
      }
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No User-Tenant is present against given value" + id
      );
    }

    UserView userView = userViewRepository.findByUserTenantId(id);
    if (userView == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found");
    }
    return new ResponseEntity<>(userView, HttpStatus.OK);
  }
}
