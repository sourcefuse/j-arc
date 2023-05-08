package com.sourcefuse.jarc.services.authservice.enums;

public enum TenantStatus {
  ACTIVE(1),
  INACTIVE(0);

  public final int label;

   TenantStatus(int label) {
    this.label = label;
  }
}
