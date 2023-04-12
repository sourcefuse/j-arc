package com.sourcefuse.jarc.services.authservice.enums;

public enum AuthErrorKeys {
  CodeExpired("Code Expired"),
  TokenExpired("Token Expired"),
  TokenInvalid("Token Invalid"),
  ClientInvalid("Client Invalid"),
  ClientVerificationFailed("Client Verification Failed"),
  ClientSecretMissing("Client Secret Missing"),
  ClientUserMissing("Client User Missing"),
  InvalidCredentials("Invalid Credentials"),
  UserVerificationFailed("User Verification Failed"),
  UnknownError("Unknown Error"),
  WrongPassword("Incorrect Password"),
  KeyInvalid("Key Invalid"),
  OtpInvalid("Otp Invalid"),
  OtpExpired("Otp Token Incorrect or Expired");

  public final String label;

  private AuthErrorKeys(String label) {
    this.label = label;
  }
}
