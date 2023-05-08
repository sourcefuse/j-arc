package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum AuthorizeErrorKeys {
    NOTALLOWEDACCESS("NotAllowedAccess");

    private final String value;

    AuthorizeErrorKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
