package com.sourcefuse.jarc.services.authservice.oauth2.services;

import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.oauth2.user.FacebookOAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.GithubOAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.GoogleOAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import java.util.Map;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public final class OAuth2UserInfoService {

  private OAuth2UserInfoService() {}

  public static OAuth2UserInfo getOAuth2UserInfo(
    String registrationId,
    Map<String, Object> attributes
  ) {
    if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else if (
      registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())
    ) {
      return new FacebookOAuth2UserInfo(attributes);
    } else if (
      registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())
    ) {
      return new GithubOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationException(
        "Sorry! Login with " + registrationId + " is not supported yet."
      );
    }
  }
}
