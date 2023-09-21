package com.sourcefuse.jarc.services.authservice.tests.unit.oauth.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.services.authservice.oauth2.auth.request.resolver.CustomOAuth2AuthorizationRequestResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Test cases for CustomOAuth2AuthorizationRequestResolver Constructor and
 * variables
 */
class RequestResolverResolveMethodTests {

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
      () -> authRequestResolver.resolve(request, "Google")
    );
  }
}
