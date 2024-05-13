package com.sourcefuse.jarc.services.authservice.tests.mocks;

import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.user.OAuth2UserInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public final class MockObjects {

  public static final String CLIENT_ID = "google";
  public static final String TOKEN_URI = "/token";
  public static final String AUTH_URI = "/auth";
  public static final String REDIRECT_URI = "/redirect";

  private static final String USER_FIRST_NAME = "ABC";
  private static final String USER_LAST_NAME = "XYZ";
  public static final String USER_USERNAME = "user@mail.com";
  public static final String USER_EMAIL = "user";

  public static ClientRegistration createMockClientRegistration() {
    return createMockClientRegistration(
      AuthorizationGrantType.AUTHORIZATION_CODE,
      Arrays.asList()
    );
  }

  public static ClientRegistration createMockClientRegistration(
    AuthorizationGrantType authorizationGrantType
  ) {
    return createMockClientRegistration(
      authorizationGrantType,
      Arrays.asList()
    );
  }

  public static ClientRegistration createMockClientRegistration(
    List<String> scopes
  ) {
    return createMockClientRegistration(
      AuthorizationGrantType.AUTHORIZATION_CODE,
      scopes
    );
  }

  private static ClientRegistration createMockClientRegistration(
    AuthorizationGrantType authorizationGrantType,
    List<String> scopes
  ) {
    return ClientRegistration
      .withRegistrationId(CLIENT_ID)
      .authorizationGrantType(authorizationGrantType)
      .clientId(CLIENT_ID)
      .tokenUri(TOKEN_URI)
      .authorizationUri(AUTH_URI)
      .userInfoUri(AUTH_URI)
      .redirectUri(REDIRECT_URI)
      .scope(scopes)
      .build();
  }

  public static User createMockUser() {
    User user = Mockito.mock(User.class);
    Mockito.when(user.getFirstName()).thenReturn(USER_FIRST_NAME);
    Mockito.when(user.getLastName()).thenReturn(USER_LAST_NAME);
    Mockito.when(user.getEmail()).thenReturn(USER_EMAIL);
    Mockito.when(user.getUsername()).thenReturn(USER_USERNAME);
    return user;
  }

  public static OidcUser createMockOidcUser() {
    OidcUser oidcUser = Mockito.mock(OidcUser.class);
    Mockito.when(oidcUser.getGivenName()).thenReturn(USER_FIRST_NAME);
    Mockito.when(oidcUser.getFamilyName()).thenReturn(USER_LAST_NAME);
    Mockito.when(oidcUser.getEmail()).thenReturn(USER_EMAIL);
    Mockito.when(oidcUser.getPreferredUsername()).thenReturn(USER_USERNAME);

    return oidcUser;
  }

  public static OAuth2UserInfo createMockOAuth2UserInfo() {
    OAuth2UserInfo oAuth2User = Mockito.mock(OAuth2UserInfo.class);
    Mockito
      .when(oAuth2User.getName())
      .thenReturn(USER_FIRST_NAME + " " + USER_LAST_NAME);
    Mockito.when(oAuth2User.getEmail()).thenReturn(USER_EMAIL);
    Mockito.when(oAuth2User.getId()).thenReturn(USER_USERNAME);
    return oAuth2User;
  }

  public static OAuth2User createMockOAuth2User() {
    OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("name", USER_FIRST_NAME + " " + USER_LAST_NAME);
    attributes.put("email", USER_EMAIL);
    attributes.put("id", USER_USERNAME);
    attributes.put("sub", USER_USERNAME);
    Mockito.when(oAuth2User.getAttributes()).thenReturn(attributes);
    return oAuth2User;
  }

  public static OidcUserRequest createMockOidcUserRequest() {
    OidcUserRequest oidcUserRequest = Mockito.mock(OidcUserRequest.class);

    Mockito
      .when(oidcUserRequest.getClientRegistration())
      .thenReturn(createMockClientRegistration());
    Mockito
      .when(oidcUserRequest.getIdToken())
      .thenReturn(Mockito.mock(OidcIdToken.class));
    return oidcUserRequest;
  }

  public static OAuth2UserRequest createMockOAuth2UserRequest() {
    OAuth2UserRequest oAuth2UserRequest = Mockito.mock(OAuth2UserRequest.class);

    Mockito
      .when(oAuth2UserRequest.getClientRegistration())
      .thenReturn(createMockClientRegistration());
    return oAuth2UserRequest;
  }

  public static UserCredential createMockUserCredentials(
    String authId,
    String authProvider
  ) {
    UserCredential userCredential = Mockito.mock(UserCredential.class);

    Mockito.when(userCredential.getAuthId()).thenReturn(authId);
    Mockito.when(userCredential.getAuthProvider()).thenReturn(authProvider);
    return userCredential;
  }

  public static UserTenant createMockUserTenant() {
    UserTenant userTenant = Mockito.mock(UserTenant.class);

    Mockito
      .when(userTenant.getRoleId())
      .thenReturn(UUID.fromString("cd3406f0-1a26-45a5-8ec6-78f7023450e0"));
    return userTenant;
  }
}
