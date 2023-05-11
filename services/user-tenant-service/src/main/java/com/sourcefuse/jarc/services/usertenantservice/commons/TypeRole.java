package com.sourcefuse.jarc.services.usertenantservice.commons;

import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.ADMIN;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.DEFAULT;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.GUEST_BOARD_VIEWER;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.GUEST_DASHBOARD_VIEWER;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.GUEST_GROUP_VIEWER;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.GUEST_TASK_VIEWER;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.GUEST_WORKSPACE_VIEWER;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.PROGRAM_MANAGER;
import static com.sourcefuse.jarc.services.usertenantservice.enums.RoleType.SUPER_ADMIN;

import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TypeRole {

  private TypeRole() {
    throw new IllegalStateException("Utility class");
  }

  public static Map<RoleType, RoleTypeMapValue> getRoleTypeMap() {
    return roleTypeMap;
  }

  static final List<String> DisallowedRoles = Collections.singletonList(
    String.valueOf(RoleTypes.OTHERS)
  );

  public static Map<RoleType, RoleTypeMapValue> roleTypeMap = new EnumMap<>(
    RoleType.class
  );

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
      PROGRAM_MANAGER,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "ProgramManager";
        }

        public int value() {
          return PROGRAM_MANAGER.ordinal();
        }
      }
    );
    roleTypeMap.put(
      GUEST_BOARD_VIEWER,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "GuestBoardViewer";
        }

        public int value() {
          return GUEST_BOARD_VIEWER.ordinal();
        }
      }
    );
    roleTypeMap.put(
      GUEST_DASHBOARD_VIEWER,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "GuestDashboardViewer";
        }

        public int value() {
          return GUEST_DASHBOARD_VIEWER.ordinal();
        }
      }
    );
    roleTypeMap.put(
      GUEST_GROUP_VIEWER,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "GuestGroupViewer";
        }

        public int value() {
          return GUEST_GROUP_VIEWER.ordinal();
        }
      }
    );
    roleTypeMap.put(
      GUEST_TASK_VIEWER,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "GuestTaskViewer";
        }

        public int value() {
          return GUEST_TASK_VIEWER.ordinal();
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
    roleTypeMap.put(
      GUEST_WORKSPACE_VIEWER,
      new RoleTypeMapValue() {
        public String permissionKey() {
          return "GuestWorkspaceViewer";
        }

        public int value() {
          return GUEST_WORKSPACE_VIEWER.ordinal();
        }
      }
    );
  }
}
