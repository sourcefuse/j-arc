package com.sourcefuse.jarc.services.usertenantservice.enums;

import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypes;

public enum RoleKey implements RoleTypes {
    ADMIN,
    OTHERS,
    DEFAULT,
    PROGRAMMANAGER,
    GUESTBOARDVIEWER,
    GUESTDASHBOARDVIEWER,
    AUTOMATION,
    GUESTTASKVIEWER,
    GUESTGROUPVIEWER,
    SUPERADMIN,
    GUESTWORKSPACEVIEWER;

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
