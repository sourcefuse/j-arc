package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.handlers.OAuth2AuthenticationSuccessHandler;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.utils.StateUtils;
import com.sourcefuse.jarc.services.authservice.oauth2.user.session.OAuth2UserSession;
import com.sourcefuse.jarc.services.authservice.providers.AuthCodeGeneratorProvider;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

class OAuth2AuthenticationSuceessHandlerTests {

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  private OAuth2AuthenticationSuccessHandler successHandler;

  private AuthCodeGeneratorProvider authCodeGeneratorProvider;

  private AuthClientRepository authClientRepository;

  private Authentication authentication;

  @BeforeEach
  void setup() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    authCodeGeneratorProvider = Mockito.mock(AuthCodeGeneratorProvider.class);
    authClientRepository = Mockito.mock(AuthClientRepository.class);
    authentication = Mockito.mock(Authentication.class);

    successHandler =
      new OAuth2AuthenticationSuccessHandler(
        authCodeGeneratorProvider,
        authClientRepository
      );
  }

  @Test
  void testNullStateException() throws IOException {
    successHandler.onAuthenticationSuccess(request, response, authentication);

    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  void testEmptyStateException() throws IOException {
    successHandler.onAuthenticationSuccess(request, response, authentication);

    String[] state = { "" };
    request.addParameter("state", state);

    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  void testContainsOnlyWhiteSpacesStateException() throws IOException {
    successHandler.onAuthenticationSuccess(request, response, authentication);

    String[] state = { "   " };
    request.addParameter("state", state);

    assertThat(response.getStatus()).isEqualTo(401);
  }

  @SuppressWarnings("unchecked")
  @Test
  void testAuthClientNotFoundException() throws IOException {
    Mockito
      .when(authClientRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.ofNullable(null));

    String[] state = { StateUtils.encode("clientId") };
    request.addParameter("state", state);
    successHandler.onAuthenticationSuccess(request, response, authentication);

    assertThat(response.getStatus()).isEqualTo(401);
  }

  @SuppressWarnings("unchecked")
  @Test
  void testAuthSuccess() throws IOException {
    Mockito
      .when(authentication.getPrincipal())
      .thenReturn(
        new OAuth2UserSession(
          new User(),
          new UserTenant(),
          new Role(),
          null,
          null,
          null
        )
      );

    Mockito
      .when(authClientRepository.findOne(Mockito.any(Specification.class)))
      .thenReturn(Optional.of(new AuthClient()));

    Mockito
      .when(
        authCodeGeneratorProvider.provide(
          Mockito.any(User.class),
          Mockito.any(UserTenant.class),
          Mockito.any(Role.class),
          Mockito.any(AuthClient.class)
        )
      )
      .thenReturn(new CodeResponse("GeneratedCode"));

    String[] state = { StateUtils.encode("clientId") };
    request.addParameter("state", state);
    successHandler.onAuthenticationSuccess(request, response, authentication);

    assertThat(response.getStatus()).isEqualTo(302);
    assertThat(response.getHeaderNames()).isNotEmpty().contains("Location");
  }
}
