package com.sourcefuse.jarc.services.usertenantservice.mocks;

import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public final class MockCurrentUserSession {

  private MockCurrentUserSession() {}

  public static void setCurrentLoggedInUser(
    UUID mockTenantId,
    UUID mockUserId,
    UUID mockUserTenantId
  ) {
    // Create a dummy user object
    CurrentUser currentUser = new CurrentUser();
    currentUser.setTenantId(mockTenantId);
    currentUser.setUserTenantId(mockUserTenantId);
    currentUser.setPermissions(new ArrayList<>());
    currentUser.setId(mockUserId);
    currentUser.setFirstName("Dummy");
    currentUser.setLastName("User");
    currentUser.setEmail("dummy.user@example.com");
    currentUser.setUsername("dummy.user@example.com");
    currentUser.setRoleType(RoleKey.ADMIN);
    currentUser.setPermissions(
      Arrays.asList(
        PermissionKey.UPDATE_TENANT.toString(),
        PermissionKey.VIEW_TENANT.toString(),
        PermissionKey.CREATE_TENANT.toString(),
        PermissionKey.DELETE_TENANT.toString(),
        PermissionKey.CREATE_ANY_USER.toString(),
        PermissionKey.DELETE_USER_GROUP.toString(),
        PermissionKey.CREATE_TENANT_USER.toString(),
        PermissionKey.VIEW_TENANT_USER.toString(),
        PermissionKey.VIEW_OWN_USER.toString(),
        PermissionKey.VIEW_ALL_USER.toString(),
        PermissionKey.UPDATE_TENANT_USER.toString(),
        PermissionKey.UPDATE_ANY_USER.toString(),
        PermissionKey.UPDATE_OWN_USER.toString(),
        PermissionKey.DELETE_TENANT_USER.toString(),
        PermissionKey.DELETE_TENANT_USER.toString(),
        PermissionKey.VIEW_USER_TENANT_PREFERENCE.toString(),
        PermissionKey.UPDATE_USER_TENANT_PREFERENCE.toString()
      )
    );
    // Create an authentication object with the dummy user and set it in the
    // SecurityContext
    UsernamePasswordAuthenticationToken auth =
      new UsernamePasswordAuthenticationToken(currentUser, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static CurrentUser getCurrentUser() {
    return (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
  }
}
