package com.sourcefuse.jarc.services.usertenantservice.commons;

public enum RoleTypes {
  ADMIN(0),
  OTHERS(1);

  private final int value;

  RoleTypes(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
