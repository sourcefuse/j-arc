package com.sourcefuse.jarc.services.authservice.enums;

public enum RoleKey {
  Admin(0),
  Default(2),
  ProgramManager(3),
  GuestBoardViewer(4),
  GuestDashboardViewer(5),
  Automation(7),
  GuestTaskViewer(8),
  GuestGroupViewer(9),
  SuperAdmin(10),
  GuestWorkspaceViewer(11);

  public final int label;

  private RoleKey(int label) {
    this.label = label;
  }
}
