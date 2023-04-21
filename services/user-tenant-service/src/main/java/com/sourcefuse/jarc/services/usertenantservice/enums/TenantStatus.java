package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum TenantStatus {

    INACTIVE(0),
    ACTIVE(1);


    private final int value;

    TenantStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}