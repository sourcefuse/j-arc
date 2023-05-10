package com.sourcefuse.jarc.services.usertenantservice.enums;

import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypeMapValue;
import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypes;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum RoleType implements RoleTypes {
  ADMIN(0),

  //doubt::
  OTHERS(1),
  DEFAULT(2),
  PROGRAM_MANAGER(3),
  GUEST_BOARD_VIEWER(4),
  GUEST_DASHBOARD_VIEWER(5),
  AUTOMATION(7),
  GUEST_TASK_VIEWER(8),
  GUEST_GROUP_VIEWER(9),
  SUPER_ADMIN(10),
  GUEST_WORKSPACE_VIEWER(11);

  public static final List<String> DisallowedRoles = Collections.singletonList(
    RoleTypes.Others
  );

  public static final Map<RoleType, RoleTypeMapValue> RoleTypeMap =
    new EnumMap<>(RoleType.class);

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

  private final Integer code;

  RoleType(Integer code) {
    this.code = code;
  }

  public Integer getCode() {
    return this.code;
  }
}
