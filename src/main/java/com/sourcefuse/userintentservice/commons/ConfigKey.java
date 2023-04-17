package com.sourcefuse.userintentservice.commons;

public enum ConfigKey {
  PasswordPolicy("password-policy"),
  DateTimeFormat("date-time format"),
  Profile("profile");

  private final String key;

  ConfigKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
