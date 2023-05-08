package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum Gender {
    MALE("M"),
    FEMALE("F"),
    OTHER("O"),
    UNKNOWN("U");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
