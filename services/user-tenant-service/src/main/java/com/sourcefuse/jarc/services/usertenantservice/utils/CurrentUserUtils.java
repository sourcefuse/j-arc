package com.sourcefuse.jarc.services.usertenantservice.utils;

import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public final class CurrentUserUtils {

  private CurrentUserUtils() {}

  public static void compareWithCurrentUserTenantId(
    UUID tenantId,
    CurrentUser currentUser
  ) {
    if (!currentUser.getTenantId().equals(tenantId)) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  public static void compareWithCurrentUsersUserId(
    UUID userId,
    CurrentUser currentUser
  ) {
    if (!currentUser.getId().equals(userId)) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  public static CurrentUser getCurrentUser() {
    return (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }

  public static CurrentUser getCurrentWithPermissions(UUID tenantId) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    compareWithCurrentUserTenantId(tenantId, currentUser);
    return currentUser;
  }

  public static void checkForViewOwnUserPermission(
    CurrentUser currentUser,
    UUID userId
  ) {
    if (
      currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_USER.toString()) &&
      !userId.equals(currentUser.getId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }

  public static void checkForViewAnyUserPermission(
    CurrentUser currentUser,
    UUID tenantId
  ) {
    if (
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_ANY_USER.toString()) &&
      !tenantId.equals(currentUser.getTenantId())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
  }
}
