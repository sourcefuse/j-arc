package com.sourcefuse.userintentservice.enums;

public enum UserConfigKey {

    LastAccessedUrl("last-accessed-url");
    private final String value;

    UserConfigKey(String value) {
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
