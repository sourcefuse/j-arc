package com.sourcefuse.jarc.services.authservice.enums;

public enum AuthErrorKeys {
  CODE_EXPIRED("Code Expired"),
  TOKEN_EXPIRED("Token Expired"),
  TOKEN_INVALID("Token Invalid"),
  CLIENT_INVALID("Client Invalid"),
  CLIENT_VERIFICATION_FAILED("Client Verification Failed"),
  CLIENT_SECRET_MISSING("Client Secret Missing"),
  CLIENT_USER_MISSING("Client User Missing"),
  INVALID_CREDENTIALS("Invalid Credentials"),
  USER_VERIFICATION_FAILED("User Verification Failed"),
  UNKNOWN_ERROR("Unknown Error"),
  WRONG_PASSWORD("Incorrect Password"),
  KEY_INVALID("Key Invalid"),
  OTP_INVALID("Otp Invalid"),
  OTP_EXPIRED("Otp Token Incorrect or Expired");

  public final String label;

   AuthErrorKeys(String label) {
    this.label = label;
  }
}
