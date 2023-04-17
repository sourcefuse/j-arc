package com.sourcefuse.userintentservice.enums;

import com.sourcefuse.userintentservice.commons.RoleTypeMapValue;
import com.sourcefuse.userintentservice.commons.RoleTypes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
