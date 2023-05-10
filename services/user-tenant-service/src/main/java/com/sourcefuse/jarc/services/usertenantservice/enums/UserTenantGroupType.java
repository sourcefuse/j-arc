package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum UserTenantGroupType {
  TENANT("Tenant");

  String value;

  UserTenantGroupType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
