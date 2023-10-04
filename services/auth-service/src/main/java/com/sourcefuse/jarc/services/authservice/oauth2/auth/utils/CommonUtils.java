package com.sourcefuse.jarc.services.authservice.oauth2.auth.utils;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public final class CommonUtils {

  private CommonUtils() {}

  public static OAuth2AuthenticationException throwUserVerificationFailed() {
    return new OAuth2AuthenticationException(
      AuthErrorKeys.USER_VERIFICATION_FAILED.toString()
    );
  }
}
