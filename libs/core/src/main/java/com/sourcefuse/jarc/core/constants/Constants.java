package com.sourcefuse.jarc.core.constants;

public class Constants {

  public static enum AuditActions {
    SAVE("SAVE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String name;

    private AuditActions(String value) {
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
}
