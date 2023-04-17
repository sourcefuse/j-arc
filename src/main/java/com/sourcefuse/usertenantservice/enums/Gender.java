package com.sourcefuse.usertenantservice.enums;

public enum Gender {
    Male("M"),
    Female("F"),
    Other("O"),
    Unknown("U");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
