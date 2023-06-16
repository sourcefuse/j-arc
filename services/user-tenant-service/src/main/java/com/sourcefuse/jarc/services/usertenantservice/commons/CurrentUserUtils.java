package com.sourcefuse.jarc.services.usertenantservice.commons;

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
}
