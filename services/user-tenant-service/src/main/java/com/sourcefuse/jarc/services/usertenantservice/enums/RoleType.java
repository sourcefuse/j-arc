package com.sourcefuse.jarc.services.usertenantservice.enums;

import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypeMapValue;
import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum RoleType implements RoleTypes {
    Admin(0),

    //doubt::
    Others(1),
    Default(2),
    ProgramManager(3),
    GuestBoardViewer(4),
    GuestDashboardViewer(5),
    Automation(7),
    GuestTaskViewer(8),
    GuestGroupViewer(9),
    SuperAdmin(10),
    GuestWorkspaceViewer(11);

    public static final List<String> DisallowedRoles = Collections.singletonList(RoleTypes.Others);

    public static final Map<RoleType, RoleTypeMapValue> RoleTypeMap = new EnumMap<>(RoleType.class);

    static {
        RoleTypeMap.put(Admin, new RoleTypeMapValue() {
            public String permissionKey() {
                return "PlatformAdmin";
            }

            public int value() {
                return Admin.ordinal();
            }
        });
        RoleTypeMap.put(Default, new RoleTypeMapValue() {
            public String permissionKey() {
                return "Default";
            }

            public int value() {
                return Default.ordinal();
            }
        });
        RoleTypeMap.put(ProgramManager, new RoleTypeMapValue() {
            public String permissionKey() {
                return "ProgramManager";
            }

            public int value() {
                return ProgramManager.ordinal();
            }
        });
        RoleTypeMap.put(GuestBoardViewer, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestBoardViewer";
            }

            public int value() {
                return GuestBoardViewer.ordinal();
            }
        });
        RoleTypeMap.put(GuestDashboardViewer, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestDashboardViewer";
            }

            public int value() {
                return GuestDashboardViewer.ordinal();
            }
        });
        RoleTypeMap.put(GuestGroupViewer, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestGroupViewer";
            }

            public int value() {
                return GuestGroupViewer.ordinal();
            }
        });
        RoleTypeMap.put(GuestTaskViewer, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestTaskViewer";
            }

            public int value() {
                return GuestTaskViewer.ordinal();
            }
        });
        RoleTypeMap.put(SuperAdmin, new RoleTypeMapValue() {
            public String permissionKey() {
                return "SuperAdmin";
            }

            public int value() {
                return SuperAdmin.ordinal();
            }
        });
        RoleTypeMap.put(GuestWorkspaceViewer, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestWorkspaceViewer";
            }

            public int value() {
                return GuestWorkspaceViewer.ordinal();
            }
        });
    }


    private final Integer code;

    private RoleType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
