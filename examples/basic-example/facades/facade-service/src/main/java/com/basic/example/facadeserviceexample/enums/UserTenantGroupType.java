package com.basic.example.facadeserviceexample.enums;

public enum UserTenantGroupType {
  TENANT("Tenant");

  private String value;

  UserTenantGroupType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
