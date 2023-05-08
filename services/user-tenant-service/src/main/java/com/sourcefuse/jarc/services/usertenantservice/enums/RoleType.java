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
    GUESTBOARDVIEWER(4),
    GUESTDASHBOARDVIEWER(5),
    AUTOMATION(7),
    GUESTTASKVIEWER(8),
    GUESTGROUPVIEWER(9),
    SUPERADMIN(10),
    GUESTWORKSPACEVIEWER(11);

    public static final List<String> DisallowedRoles = Collections.singletonList(RoleTypes.Others);

    public static final Map<RoleType, RoleTypeMapValue> RoleTypeMap = new EnumMap<>(RoleType.class);

    static {
        RoleTypeMap.put(ADMIN, new RoleTypeMapValue() {
            public String permissionKey() {
                return "PlatformAdmin";
            }

            public int value() {
                return ADMIN.ordinal();
            }
        });
        RoleTypeMap.put(DEFAULT, new RoleTypeMapValue() {
            public String permissionKey() {
                return "Default";
            }

            public int value() {
                return DEFAULT.ordinal();
            }
        });
        RoleTypeMap.put(PROGRAM_MANAGER, new RoleTypeMapValue() {
            public String permissionKey() {
                return "ProgramManager";
            }

            public int value() {
                return PROGRAM_MANAGER.ordinal();
            }
        });
        RoleTypeMap.put(GUESTBOARDVIEWER, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestBoardViewer";
            }

            public int value() {
                return GUESTBOARDVIEWER.ordinal();
            }
        });
        RoleTypeMap.put(GUESTDASHBOARDVIEWER, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestDashboardViewer";
            }

            public int value() {
                return GUESTDASHBOARDVIEWER.ordinal();
            }
        });
        RoleTypeMap.put(GUESTGROUPVIEWER, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestGroupViewer";
            }

            public int value() {
                return GUESTGROUPVIEWER.ordinal();
            }
        });
        RoleTypeMap.put(GUESTTASKVIEWER, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestTaskViewer";
            }

            public int value() {
                return GUESTTASKVIEWER.ordinal();
            }
        });
        RoleTypeMap.put(SUPERADMIN, new RoleTypeMapValue() {
            public String permissionKey() {
                return "SuperAdmin";
            }

            public int value() {
                return SUPERADMIN.ordinal();
            }
        });
        RoleTypeMap.put(GUESTWORKSPACEVIEWER, new RoleTypeMapValue() {
            public String permissionKey() {
                return "GuestWorkspaceViewer";
            }

            public int value() {
                return GUESTWORKSPACEVIEWER.ordinal();
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
