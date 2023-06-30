package com.sourcefuse.jarc.services.auditservice.constant;

import com.sourcefuse.jarc.core.constants.AuditPermissions;
import com.sourcefuse.jarc.core.constants.NotificationPermissions;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class TestConstants {

  public static UUID mockUserId = UUID.fromString(
    "d262e1bf-1be8-3bac-ce49-166324df1e33"
  );

  public static CurrentUser createMockCurrentUser() {
    // Create a dummy user object
    CurrentUser currentUser = new CurrentUser();
    currentUser.setId(mockUserId);
    currentUser.setFirstName("Dummy");
    currentUser.setLastName("User");
    currentUser.setEmail("dummy.user@example.com");
    currentUser.setUsername("dummy.user@example.com");
    currentUser.setPermissions(
      Arrays.asList(
        AuditPermissions.CREATE_AUDIT,
        AuditPermissions.VIEW_AUDIT,
        AuditPermissions.UPDATE_AUDIT,
        AuditPermissions.DELETE_AUDIT,
        NotificationPermissions.CAN_GET_NOTIFICATION_ACCESS,
        NotificationPermissions.CREATE_NOTIFICATION,
        NotificationPermissions.DELETE_NOTIFICATION,
        NotificationPermissions.UPDATE_NOTIFICATION,
        NotificationPermissions.VIEW_NOTIFICATION
      )
    );

    return currentUser;
  }
}
