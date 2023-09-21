package com.sourcefuse.jarc.services.authservice.tests.unit.oauth.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.services.authservice.oauth2.auth.request.resolver.CustomOAuth2AuthorizationRequestResolver;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * Test cases for CustomOAuth2AuthorizationRequestResolver Constructor and
 * variables
 */
class RequestResolverContructorAndVariableTests {

  private CustomOAuth2AuthorizationRequestResolver authRequestResolver;
  private ClientRegistrationRepository clientRegistrationRepository;
  private MockHttpServletRequest request;

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
