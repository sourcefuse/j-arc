package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum UserTenantGroupType {
    Tenant("Tenant");

     String value;

    private UserTenantGroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

