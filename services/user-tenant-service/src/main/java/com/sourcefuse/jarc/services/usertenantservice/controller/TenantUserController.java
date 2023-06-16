package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.commons.CurrentUserUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.service.DeleteTenantUserService;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserService;
import com.sourcefuse.jarc.services.usertenantservice.service.UpdateTenantUserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/tenants/{id}/users")
@RequiredArgsConstructor
public class TenantUserController {

  private final TenantUserService tenantUserService;
  private final UpdateTenantUserService updateTenantUserService;
  private final DeleteTenantUserService deleteTenantUserService;

  @Value("${tenant.id.not.specified}")
  private String tenantIdNotSpecified;

  @PostMapping
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

    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    return new ResponseEntity<>(
      tenantUserService.create(userDto, currentUser, option),
      HttpStatus.CREATED
    );
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUserTenantById(
    @PathVariable("id") UUID id
  ) {
    CurrentUserUtils.getCurrentWithPermissions(id);
    return new ResponseEntity<>(
      tenantUserService.getUserView(id),
      HttpStatus.OK
    );
  }

  @GetMapping("/view-all")
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
  public ResponseEntity<Count> userTenantCount(@PathVariable("id") UUID id) {
    CurrentUserUtils.getCurrentWithPermissions(id);
    long userCount;
    userCount = tenantUserService.getUserView(id).size();

    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(
      Count.builder().totalCount(userCount).build(),
      HttpStatus.OK
    );
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> findAllUsers(
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentWithPermissions(id);
    CurrentUserUtils.compareWithCurrentUsersUserId(userId, currentUser);

    UserView userView = tenantUserService.findById(userId);
    //nonRestrictedUserViewRepo ::doubt
    return new ResponseEntity<>(userView, HttpStatus.OK);
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<Object> updateUserById(
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

  @DeleteMapping("/{userId}")
  public ResponseEntity<Object> deleteUserTenantById(
    @PathVariable("id") UUID id,
    @PathVariable("userId") UUID userId
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    deleteTenantUserService.deleteUserById(currentUser, userId, id);
    return new ResponseEntity<>("User DELETE success", HttpStatus.NO_CONTENT);
  }
}
