package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.service.DeleteTenantUserService;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserService;
import com.sourcefuse.jarc.services.usertenantservice.service.UpdateTenantUserService;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/tenants/{id}/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TenantUserController {

  private final TenantUserService tenantUserService;
  private final UpdateTenantUserService updateTenantUserService;
  private final DeleteTenantUserService deleteTenantUserService;

  @Value("${tenant.id.not.specified}")
  private String tenantIdNotSpecified;

  @PostMapping
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.CREATE_ANY_USER +
    "','" +
    PermissionKeyConstants.CREATE_TENANT_USER +
    "','" +
    PermissionKeyConstants.CREATE_TENANT_USER_RESTRICTED +
    "')"
  )
  public ResponseEntity<UserDto> createUserTenants(
    @Valid @RequestBody UserDto userDto,
    @PathVariable("id") UUID id
  ) {
    userDto.setTenantId(id);
    userDto
      .getUserDetails()
      .setEmail(userDto.getUserDetails().getEmail().toLowerCase());
    userDto
      .getUserDetails()
      .setUsername(userDto.getUserDetails().getUsername().toLowerCase());
    /** map created for ruling with options */
    Map<String, String> option = new HashMap<>();
    option.put("authId", userDto.getAuthId());
    option.put("authProvider", userDto.getAuthProvider());
    log.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    MockCurrentUserSession.setCurrentLoggedInUser(
            userDto.getTenantId(),
            null,
            null
    );
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    return new ResponseEntity<>(
      new UserDto(),
      HttpStatus.CREATED
    );
  }

  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.VIEW_ANY_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER_RESTRICTED +
    "')"
  )
  public ResponseEntity<List<UserDto>> getUserTenantById(
    @PathVariable("id") UUID id
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    return new ResponseEntity<>(
      tenantUserService.getUserView(id, currentUser),
      HttpStatus.OK
    );
  }

  @GetMapping("/view-all")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_ALL_USER +
    "')"
  )
  public ResponseEntity<List<UserDto>> findAllUsers(
    @PathVariable("id") UUID id
  ) {
    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(
      tenantUserService.getAllUsers(id),
      HttpStatus.OK
    );
  }

  @GetMapping("/count")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.VIEW_ANY_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER_RESTRICTED +
    "')"
  )
  public ResponseEntity<CountResponse> userTenantCount(
    @PathVariable("id") UUID id
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    long userCount = tenantUserService.getUserView(id, currentUser).size();

    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(
      CountResponse.builder().count(userCount).build(),
      HttpStatus.OK
    );
  }

  @GetMapping("/{userId}")
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
  public ResponseEntity<UserView> findAllUsers(
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    UserView userView = tenantUserService.findById(userId, id, currentUser);
    return new ResponseEntity<>(userView, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping("/{userId}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.UPDATE_ANY_USER +
    "','" +
    PermissionKeyConstants.UPDATE_OWN_USER +
    "','" +
    PermissionKeyConstants.UPDATE_TENANT_USER +
    "','" +
    PermissionKeyConstants.UPDATE_TENANT_USER_RESTRICTED +
    "')"
  )
  public ResponseEntity<String> updateUserById(
    @RequestBody UserView userView,
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    if (currentUser.getId().equals(userId) && userView.getRoleId() != null) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    if (userView.getUsername() != null) {
      userView.setUsername(userView.getUsername().toLowerCase());
    }

    updateTenantUserService.updateById(currentUser, userId, userView, id);

    return new ResponseEntity<>("User PATCH success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @DeleteMapping("/{userId}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.DELETE_ANY_USER +
    "','" +
    PermissionKeyConstants.DELETE_TENANT_USER +
    "','" +
    PermissionKeyConstants.DELETE_TENANT_USER_RESTRICTED +
    "')"
  )
  public ResponseEntity<String> deleteUserTenantById(
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    deleteTenantUserService.deleteUserById(currentUser, userId, id);
    return new ResponseEntity<>("User DELETE success", HttpStatus.NO_CONTENT);
  }
}
