package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum UserStatus {
    REGISTERED(0),
    ACTIVE(1),
    INACTIVE(2),
    PASSWORD_CHANGE_NEEDED(3),
    REJECTED(4);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
