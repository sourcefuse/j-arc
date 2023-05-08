package com.sourcefuse.jarc.services.authservice.enums;

public enum RoleKey {
  ADMIN(0),
  DEFAULT(2),
  PROGRAM_MANAGER(3),
  GUEST_BOARD_VIEWER(4),
  GUEST_DASHBOARD_VIEWER(5),
  AUTOMATION(7),
  GUEST_TASK_VIEWER(8),
  GUEST_GROUP_VIEWER(9),
  SUPER_ADMIN(10),
  GUEST_WORKSPACE_VIEWER(11);

  public final int label;

  RoleKey(int label) {
    this.label = label;
  }
}
