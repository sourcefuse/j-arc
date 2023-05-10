package com.sourcefuse.jarc.core.constants;

public enum AuditActions {
    SAVE("SAVE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String name;

    AuditActions(String value) {
        this.name = value;
    }

    public String value() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
