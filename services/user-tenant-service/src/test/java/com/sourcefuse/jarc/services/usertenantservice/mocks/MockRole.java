package com.sourcefuse.jarc.services.usertenantservice.mocks;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public final class MockRole {

  private MockRole() {}

  public static final UUID ROLE_ID = UUID.fromString(
          "45e6bb6b-3150-4cf3-9ca2-3bb299f5e35f"
  );

  public static Role getRoleObj() {
    Role role = new Role();
    role.setId(ROLE_ID);
    role.setName("John");
    role.setRoleType(RoleKey.ADMIN);
    role.setPermissions(new ArrayList<>(Arrays.asList("create", "update")));
    role.setAllowedClients(new ArrayList<>(Arrays.asList("sf", "tcs")));
    return role;
  }
}
