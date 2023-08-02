package com.sourcefuse.jarc.services.usertenantservice.constant;

import com.sourcefuse.jarc.core.constants.NotificationPermissions;
import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.Arrays;
import java.util.UUID;

public class TestConstants {

  public static UUID mockUserId = UUID.fromString(
    "43e189e6-00bc-482a-94be-5b2e2cc1c3ac"
  );

  public static CurrentUser createMockCurrentUser() {
    // Create a dummy user object
    CurrentUser currentUser = new CurrentUser();
    currentUser.setId(mockUserId);
    currentUser.setFirstName("Dummy");
    currentUser.setLastName("User");
    currentUser.setEmail("dummy.user@example.com");
    currentUser.setUsername("dummy.user@example.com");
    currentUser.setRoleType(RoleKey.ADMIN);
    currentUser.setPermissions(
      Arrays.asList(NotificationPermissions.CREATE_NOTIFICATION ,
              PermissionKey.VIEW_OWN_TENANT.toString(),
        PermissionKey.UPDATE_TENANT.toString(),
        PermissionKey.UPDATE_OWN_TENANT.toString(),
        PermissionKey.VIEW_TENANT.toString(),
        PermissionKey.CREATE_TENANT.toString(),
        PermissionKey.DELETE_TENANT.toString(),
        PermissionKey.DELETE_USER_GROUP.toString(),
        PermissionKey.CREATE_ROLES.toString(),
        PermissionKey.VIEW_ROLES.toString(),
        PermissionKey.UPDATE_ROLES.toString(),
        PermissionKey.DELETE_ROLES.toString(),
        PermissionKey.NOT_ALLOWED.toString(),
        PermissionKey.CREATE_USER_GROUP.toString(),
        PermissionKey.VIEW_USER_GROUP_LIST.toString(),
        PermissionKey.UPDATE_USER_GROUP.toString(),
        PermissionKey.DELETE_USER_GROUP.toString(),
        PermissionKey.ADD_MEMBER_TO_USER_GROUP.toString(),
        PermissionKey.UPDATE_MEMBER_IN_USER_GROUP.toString(),
        PermissionKey.REMOVE_MEMBER_FROM_USER_GROUP.toString(),
        PermissionKey.CREATE_TENANT_USER.toString(),
        PermissionKey.VIEW_TENANT_USER.toString(),
        PermissionKey.VIEW_ALL_USER.toString(),
        PermissionKey.UPDATE_TENANT_USER.toString(),
        PermissionKey.DELETE_TENANT_USER.toString(),
        PermissionKey.VIEW_USER_TENANT_PREFERENCE.toString(),
        PermissionKey.UPDATE_USER_TENANT_PREFERENCE.toString()
      )
    );

    return currentUser;
  }
}
