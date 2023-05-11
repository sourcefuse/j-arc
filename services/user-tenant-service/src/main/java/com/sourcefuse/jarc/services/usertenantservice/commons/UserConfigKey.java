package com.sourcefuse.jarc.services.usertenantservice.commons;

public enum UserConfigKey {
  LAST_ACCESSED_URL("last-accessed-url");

  private final String key;

  UserConfigKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
