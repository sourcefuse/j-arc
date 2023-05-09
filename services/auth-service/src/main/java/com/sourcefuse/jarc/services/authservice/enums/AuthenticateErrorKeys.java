package com.sourcefuse.jarc.services.authservice.enums;

public enum AuthenticateErrorKeys {
  USER_DOES_NOT_EXISTS("UserDoesNotExist"),
  PASSWORD_CANNOT_BE_CHANGED("PasswordCannotBeChangedForExternalUser"),
  USER_INACTIVE("UserInactive"),
  TOKEN_REVOKED("TokenRevoked"),
  TOKEN_MISSING("TokenMissing"),
  TEMP_PASSWORD_LOGIN_DISALLOWED("TempPasswordLoginDisallowed"),
  PASSWORD_INVALID("PasswordInvalid"),
  UNPROCESSABLE_DATA("UnprocessableData"),
  PASSWORD_EXPIRY_ERROR("PasswordExpiryError");

  public final String label;

  AuthenticateErrorKeys(String label) {
    this.label = label;
  }
}
