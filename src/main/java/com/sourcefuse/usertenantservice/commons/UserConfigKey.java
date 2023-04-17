package com.sourcefuse.usertenantservice.commons;

public enum UserConfigKey {
  LastAccessedUrl("last-accessed-url");

  private final String key;

  UserConfigKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
