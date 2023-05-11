package com.sourcefuse.jarc.services.usertenantservice.enums;

import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypeMapValue;
import com.sourcefuse.jarc.services.usertenantservice.commons.RoleTypes;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum RoleType {
  ADMIN(0),
  //doubt::
  OTHERS(1),
  DEFAULT(2),
  PROGRAM_MANAGER(3),
  GUEST_BOARD_VIEWER(4),
  GUEST_DASHBOARD_VIEWER(5),
  AUTOMATION(7),
  GUEST_TASK_VIEWER(8),
  GUEST_GROUP_VIEWER(9),
  SUPER_ADMIN(10),
  GUEST_WORKSPACE_VIEWER(11);

  private final Integer code;

  RoleType(Integer code) {
    this.code = code;
  }

  public Integer getCode() {
    return this.code;
  }
}
