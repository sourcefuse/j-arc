package com.sourcefuse.jarc.services.authservice.enums;

public enum AuthenticateErrorKeys {
  USER_DOES_NOT_EXISTS,
  PASSWORD_CANNOT_BE_CHANGED,
  USER_INACTIVE,
  TOKEN_REVOKED,
  TOKEN_MISSING,
  TEMP_PASSWORD_LOGIN_DISALLOWED,
  PASSWORD_INVALID,
  UNPROCESSABLE_DATA,
  PASSWORD_EXPIRY_ERROR;

}
