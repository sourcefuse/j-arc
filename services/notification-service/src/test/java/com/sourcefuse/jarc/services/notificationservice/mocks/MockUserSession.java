package com.sourcefuse.jarc.services.notificationservice.mocks;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public final class MockUserSession {

  public static final UUID MOCK_USER_ID = UUID.fromString(
    "d262e1bf-1be8-3bac-ce49-166324df1e33"
  );

  public static void setCurrentLoggedInUser() {
    // Create a dummy user object
    CurrentUser currentUser = new CurrentUser();
    currentUser.setId(MOCK_USER_ID);
    currentUser.setFirstName("Dummy");
    currentUser.setLastName("User");
    currentUser.setEmail("dummy.user@example.com");
    currentUser.setUsername("dummy.user@example.com");

    // Create an authentication object with the dummy user and set it in the
    // SecurityContext
    UsernamePasswordAuthenticationToken auth =
      new UsernamePasswordAuthenticationToken(currentUser, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
