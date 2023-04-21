package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum RoleKey {
    Admin,
    Default(2),
    ProgramManager,
    GuestBoardViewer,
    GuestDashboardViewer,
    Automation(7),
    GuestTaskViewer,
    GuestGroupViewer,
    SuperAdmin(10),
    GuestWorkspaceViewer(11);

    private final int value;

    RoleKey() {
        this.value = ordinal();
    }

    RoleKey(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
