package com.sourcefuse.jarc.services.usertenantservice.utils;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public final class MockCurrentUserSession {

  private MockCurrentUserSession() {}

  public static void setCurrentLoggedInUser(
    UUID MOCK_TENANT_ID,
    UUID MOCK_USER_ID,
    UUID MOCK_USER_TENANT_ID
  ) {
    // Create a dummy user object
    CurrentUser currentUser = new CurrentUser();
    currentUser.setTenantId(MOCK_TENANT_ID);
    currentUser.setUserTenantId(MOCK_USER_TENANT_ID);
    currentUser.setPermissions(new ArrayList<>());
    currentUser.setId(MOCK_USER_ID);
    currentUser.setFirstName("Dummy");
    currentUser.setLastName("User");
    currentUser.setEmail("dummy.user@example.com");
    currentUser.setUsername("dummy.user@example.com");
    currentUser.setRoleType(RoleKey.ADMIN);
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
