package com.sourcefuse.jarc.services.usertenantservice.commons;

import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.ADMIN;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.DEFAULT;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.SUPER_ADMIN;

import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TypeRole {

  static final List<String> DisallowedRoles = Collections.singletonList(
    String.valueOf(RoleTypes.OTHERS)
  );
  private static Map<RoleType, RoleTypeMapValue> roleTypeMap = new EnumMap<>(
    RoleType.class
  );

  private TypeRole() {
    throw new IllegalStateException("Utility class");
  }

  public static Map<RoleType, RoleTypeMapValue> getRoleTypeMap() {
    return new HashMap<>(roleTypeMap);
  }

  static {
    roleTypeMap.put(
      ADMIN,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "PlatformAdmin";
        }

        public int value() {
          return ADMIN.ordinal();
        }
      }
    );
    roleTypeMap.put(
      DEFAULT,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "Default";
        }

        public int value() {
          return DEFAULT.ordinal();
        }
      }
    );
    roleTypeMap.put(
      SUPER_ADMIN,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "SuperAdmin";
        }

        public int value() {
          return SUPER_ADMIN.ordinal();
        }
      }
    );
  }
}
