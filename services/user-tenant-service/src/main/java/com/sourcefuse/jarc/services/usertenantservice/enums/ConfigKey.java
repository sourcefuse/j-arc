package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum ConfigKey {
  PASSWORD_POLICY("password-policy"),
  DATE_TIME_FORMAT("date-time format"),
  PROFILE("profile");

  private final String key;

  ConfigKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
