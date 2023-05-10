package com.sourcefuse.jarc.services.usertenantservice.enums;

import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypes;

public enum RoleKey implements RoleTypes {
  ADMIN,
  OTHERS,
  DEFAULT,
  PROGRAM_MANAGER,
  GUEST_BOARD_VIEWER,
  GUEST_DASHBOARD_VIEWER,
  AUTOMATION,
  GUEST_TASK_VIEWER,
  GUEST_GROUP_VIEWER,
  SUPER_ADMIN,
  GUEST_WORKSPACE_VIEWER;

  private final int value;

  RoleKey() {
    this.value = ordinal();
  }

  RoleKey(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
