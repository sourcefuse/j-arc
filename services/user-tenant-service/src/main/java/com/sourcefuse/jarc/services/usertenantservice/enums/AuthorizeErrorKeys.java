package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum AuthorizeErrorKeys {
  NOT_ALLOWED_ACCESS("NotAllowedAccess");

  String value;

  AuthorizeErrorKeys(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
