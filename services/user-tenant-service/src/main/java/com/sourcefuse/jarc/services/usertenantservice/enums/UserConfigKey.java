package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum UserConfigKey {
  LAST_ACCESSED_URL("last-accessed-url");

  private final String value;

  UserConfigKey(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
