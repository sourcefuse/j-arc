package com.sourcefuse.jarc.services.authservice.tests.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.handlers.OAuth2AuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

class OAuth2AuthenticationFailureHandlerTests {

  private HttpServletRequest request;
  private HttpServletResponse response;

  private OAuth2AuthenticationFailureHandler failureHandler;

  @BeforeEach
  void setup() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    failureHandler = new OAuth2AuthenticationFailureHandler();
  }

  @Test
  void testResponseContainsStatus401() throws IOException {
    failureHandler.onAuthenticationFailure(
      request,
      response,
      new OAuth2AuthenticationException(
        AuthErrorKeys.USER_VERIFICATION_FAILED.toString()
      )
    );

    assertThat(response.getStatus()).isEqualTo(401);
  }
}
