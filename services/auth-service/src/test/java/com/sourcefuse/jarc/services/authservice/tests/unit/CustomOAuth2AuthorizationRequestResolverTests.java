package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.services.authservice.oauth2.auth.request.resolver.CustomOAuth2AuthorizationRequestResolver;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Test cases for CustomOAuth2AuthorizationRequestResolver Constructor and
 * variables
 */
class CustomOAuth2AuthorizationRequestResolverTests {

  private String clientId = "google";
  private String tokenURI = "/token";
  private String authURI = "/auth";
  private String redirectURI = "/redirect";

  private CustomOAuth2AuthorizationRequestResolver authRequestResolver;
  private ClientRegistrationRepository clientRegistrationRepository;
  private MockHttpServletRequest request;

  private ClientRegistration clientRegistration;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    request.setMethod("GET");
    request.setServletPath("/oauth2/authorization/google");
    request.setRequestURI("/oauth2/authorization/google");
    clientRegistrationRepository =
      Mockito.mock(ClientRegistrationRepository.class);
    authRequestResolver =
      new CustomOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository
      );
    clientRegistration =
      ClientRegistration
        .withRegistrationId(clientId)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .clientId(clientId)
        .tokenUri(tokenURI)
        .authorizationUri(authURI)
        .redirectUri(redirectURI)
        .build();
  }

  @Test
  void testResolveReturnNullWhenRegistrationIdIsNull() {
    request.setRequestURI("/oauth2/");
    request.setServletPath("/oauth2/");
    OAuth2AuthorizationRequest authRequest = authRequestResolver.resolve(
      request
    );

    assertThat(authRequest).isNull();
  }

  @Test
  void testResolveThrowExceptionWhenClientRegistrationIsNull() {
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(null);

    assertThrows(
      IllegalArgumentException.class,
      () -> authRequestResolver.resolve(request)
    );
  }

  @Test
  void testResolveReturnNullWhenNullRegistrationIdProvidedInConstructor() {
    OAuth2AuthorizationRequest authRequest = authRequestResolver.resolve(
      request,
      null
    );

    assertThat(authRequest).isNull();
  }

  @Test
  void testResolveThrowExceptionWhenRegistrationIdIsProvidedInConstructor() {
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(null);

    assertThrows(
      IllegalArgumentException.class,
      () -> authRequestResolver.resolve(request, clientId)
    );
  }

  @Test
  void testResolveWithSuccessWithAction() {
    request.addParameter("action", "authorize");
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(clientRegistration);

    authRequestResolver.resolve(request, clientId);
  }

  @Test
  void testResolveThrowErrorWhenClientRegistrationWithAuthrorizationGrantTypeIsJWTBearer() {
    clientRegistration =
      ClientRegistration
        .withRegistrationId(clientId)
        .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
        .clientId(clientId)
        .tokenUri("/token")
        .authorizationUri("/auth")
        .redirectUri("/redirect")
        .build();
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(clientRegistration);

    assertThrows(
      IllegalArgumentException.class,
      () -> authRequestResolver.resolve(request, clientId)
    );
  }

  @Test
  void testResolveWithSuccessWithClientRegistrationScopeIsNonEmpty() {
    clientRegistration =
      ClientRegistration
        .withRegistrationId(clientId)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .clientId(clientId)
        .tokenUri("/token")
        .authorizationUri("/auth")
        .redirectUri("/redirect")
        .scope(Arrays.asList("profile"))
        .build();
    request.addParameter("action", "authorize");
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(clientRegistration);

    authRequestResolver.resolve(request, clientId);
  }

  @Test
  void testResolveWithSuccessWithClientRegistrationScopeIsNonEmptyCOntainsOpenId() {
    clientRegistration =
      ClientRegistration
        .withRegistrationId(clientId)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .clientId(clientId)
        .tokenUri("/token")
        .authorizationUri("/auth")
        .redirectUri("/redirect")
        .scope(Arrays.asList("profile", "openid"))
        .build();
    request.addParameter("action", "authorize");
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(clientRegistration);

    authRequestResolver.resolve(request, clientId);
  }

  @Test
  void testResolveWithSuccessWhenPathNotContainPathDelimeter() {
    request.setServletPath("oauth2/authorization/google");
    request.setRequestURI("oauth2/authorization/google");
    Mockito
      .when(
        clientRegistrationRepository.findByRegistrationId(Mockito.anyString())
      )
      .thenReturn(clientRegistration);

    authRequestResolver.resolve(request, clientId);
  }

  @Test
  void testInstanceThrowExceptionWhenClientRepositoryIsNull() {
    assertThrows(
      IllegalArgumentException.class,
      () -> new CustomOAuth2AuthorizationRequestResolver(null)
    );
  }

  @Test
  void testInstanceThrowExceptionWhenClientRepositoryIsNullWithBaseUrl() {
    assertThrows(
      IllegalArgumentException.class,
      () ->
        new CustomOAuth2AuthorizationRequestResolver(
          null,
          "auth/registrationId"
        )
    );
  }

  @Test
  void testInstanceThrowExceptionWhenAuthorizationBaseUriIsNull() {
    assertThrows(
      IllegalArgumentException.class,
      () ->
        new CustomOAuth2AuthorizationRequestResolver(
          clientRegistrationRepository,
          null
        )
    );
  }

  @Test
  void testInstanceThrowExceptionWhenAuthorizationBaseUriIsEmpty() {
    assertThrows(
      IllegalArgumentException.class,
      () ->
        new CustomOAuth2AuthorizationRequestResolver(
          clientRegistrationRepository,
          ""
        )
    );
  }

  @Test
  void testInstanceThrowExceptionWhenAuthorizationBaseUriContainsWhiteSpace() {
    assertThrows(
      IllegalArgumentException.class,
      () ->
        new CustomOAuth2AuthorizationRequestResolver(
          clientRegistrationRepository,
          "   "
        )
    );
  }

  @Test
  void testSetAuthorizationRequestCustomizerThrowExceptionWhenNullValueIsProvided() {
    assertThrows(
      IllegalArgumentException.class,
      () -> authRequestResolver.setAuthorizationRequestCustomizer(null)
    );
  }

  @SuppressWarnings("unchecked")
  @Test
  void testSuccessWhenSetAuthorizationRequestCustomizerIsProvided()
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    authRequestResolver.setAuthorizationRequestCustomizer(
      Mockito.mock(Consumer.class)
    );
    Field authorizationRequestCustomizerField =
      CustomOAuth2AuthorizationRequestResolver.class.getDeclaredField(
          "authorizationRequestCustomizer"
        );
    authorizationRequestCustomizerField.setAccessible(true);

    assertThat(authorizationRequestCustomizerField.get(authRequestResolver))
      .isNotNull();
  }

  @Test
  void testSuccesWhenIntanceCreatedWithNotNullValues()
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    authRequestResolver =
      new CustomOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository,
        "auth/registrationId"
      );
    Field clientRepositoryField =
      CustomOAuth2AuthorizationRequestResolver.class.getDeclaredField(
          "clientRegistrationRepository"
        );
    clientRepositoryField.setAccessible(true);
    Field antMatcherField =
      CustomOAuth2AuthorizationRequestResolver.class.getDeclaredField(
          "authorizationRequestMatcher"
        );
    antMatcherField.setAccessible(true);

    assertThat(clientRepositoryField.get(authRequestResolver)).isNotNull();
    assertThat(antMatcherField.get(authRequestResolver)).isNotNull();
  }
}
