package com.sourcefuse.jarc.services.authservice.oauth2.auth.handlers;

import com.sourcefuse.jarc.authlib.utils.Utils;
import com.sourcefuse.jarc.core.dtos.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OAuth2AuthenticationFailureHandler
  extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception
  ) throws IOException {
    log.error(null, exception);
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      exception.getMessage(),
      request.getRequestURI()
    );
    String json = Utils
      .getObjectMapperInstance()
      .writeValueAsString(errorDetails);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write(json);
  }
}
