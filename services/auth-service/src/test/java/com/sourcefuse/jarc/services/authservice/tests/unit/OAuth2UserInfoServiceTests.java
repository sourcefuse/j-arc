package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sourcefuse.jarc.services.authservice.oauth2.services.OAuth2UserInfoService;
import com.sourcefuse.jarc.services.authservice.oauth2.user.FacebookOAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.GithubOAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.GoogleOAuth2UserInfo;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

class OAuth2UserInfoServiceTests {

  @Test
  void testGetOAuth2UserInfoSuccessForProviderGoogle() {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoService.getOAuth2UserInfo(
      "google",
      new HashMap<String, Object>()
    );
    assertTrue(oAuth2UserInfo instanceof GoogleOAuth2UserInfo);
  }

  @Test
  void testGetOAuth2UserInfoSuccessForProviderFacebook() {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoService.getOAuth2UserInfo(
      "facebook",
      new HashMap<String, Object>()
    );
    assertTrue(oAuth2UserInfo instanceof FacebookOAuth2UserInfo);
  }

  @Test
  void testGetOAuth2UserInfoSuccessForProviderGithub() {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoService.getOAuth2UserInfo(
      "github",
      new HashMap<String, Object>()
    );
    assertTrue(oAuth2UserInfo instanceof GithubOAuth2UserInfo);
  }

  @Test
  void testGetOAuth2UserInfoThrowErrorWhenProviderIsNotSupported() {
    Map<String, Object> attributes = new HashMap<>();
    assertThrows(
      OAuth2AuthenticationException.class,
      () ->
        OAuth2UserInfoService.getOAuth2UserInfo(
          "instagram",
          attributes
        )
    );
  }
}
