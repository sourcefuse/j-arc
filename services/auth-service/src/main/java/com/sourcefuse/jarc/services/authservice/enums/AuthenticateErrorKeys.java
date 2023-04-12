package com.sourcefuse.jarc.services.authservice.enums;

public enum AuthenticateErrorKeys {
  UserDoesNotExist("UserDoesNotExist"),
  PasswordCannotBeChanged("PasswordCannotBeChangedForExternalUser"),
  UserInactive("UserInactive"),
  TokenRevoked("TokenRevoked"),
  TokenMissing("TokenMissing"),
  TempPasswordLoginDisallowed("TempPasswordLoginDisallowed"),
  PasswordInvalid("PasswordInvalid"),
  UnprocessableData("UnprocessableData"),
  PasswordExpiryError("PasswordExpiryError");

  public final String label;

  private AuthenticateErrorKeys(String label) {
    this.label = label;
  }
}
