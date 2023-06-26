package com.sourcefuse.jarc.services.usertenantservice.mocks;

import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserTenantGroupType;
import java.util.Arrays;
import java.util.UUID;

public final class MockGroup {

  private MockGroup() {}

  public static final UUID GROUP_ID =UUID.fromString(
          "45e6bb6b-3150-4cf3-9ca2-3bb299f5e36f"
  );

  public static final UUID USER_GROUP_ID = UUID.fromString(
          "45e6bb6b-3150-4cf3-9ca2-3bb299f5e37f"
  );
  public static final UUID GROUP_ID_TWO =UUID.fromString(
          "45e6bb6b-3150-4cf3-9ca2-3bb298f5e38f"
  );

  public static Group getGroupObj() {
    Group group = new Group();
    group.setId(GROUP_ID);
    group.setName("John");
    group.setDescription("Group Unit Test");
    group.setPhotoUrl("photo.google.com");
    group.setGroupType(UserTenantGroupType.TENANT);
    group.setUserGroups(Arrays.asList(new UserGroup(), new UserGroup()));
    group.setTenantId(MockTenantUser.TENANT_ID);
    return group;
  }

  public static UserGroup getUserGroupObj() {
    UserGroup userGroup = new UserGroup();
    userGroup.setId(USER_GROUP_ID);
    userGroup.setUsrTnt(MockTenantUser.USER_TENANT_ID);
    userGroup.setGrp(GROUP_ID);
    userGroup.setTenantId(MockTenantUser.TENANT_ID);
    return userGroup;
  }
}
