package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum UserConfigKey {
  LAST_ACCESSED_URL(0);

  private final Integer value;

  UserConfigKey(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
