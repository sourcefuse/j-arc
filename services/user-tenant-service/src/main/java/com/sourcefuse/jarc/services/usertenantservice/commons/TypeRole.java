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
import lombok.Getter;

public class TypeRole {

  public static final List<String> DisallowedRoles = Collections.singletonList(
    String.valueOf(RoleTypes.OTHERS)
  );

  public static Map<RoleType, RoleTypeMapValue> RoleTypeMap = new EnumMap<>(
    RoleType.class
  );

  static {
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
    RoleTypeMap.put(
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
