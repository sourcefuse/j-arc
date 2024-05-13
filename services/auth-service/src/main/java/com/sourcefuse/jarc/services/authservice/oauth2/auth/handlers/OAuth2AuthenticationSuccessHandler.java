package com.sourcefuse.jarc.services.authservice.oauth2.auth.handlers;

import com.sourcefuse.jarc.authlib.utils.Utils;
import com.sourcefuse.jarc.core.dtos.ErrorDetails;
import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.utils.StateUtils;
import com.sourcefuse.jarc.services.authservice.oauth2.user.session.OAuth2UserSession;
import com.sourcefuse.jarc.services.authservice.providers.AuthCodeGeneratorProvider;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler
  extends SimpleUrlAuthenticationSuccessHandler {

  private final AuthCodeGeneratorProvider authCodeGeneratorProvider;

  private final AuthClientRepository authClientRepository;

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  ) throws IOException {
    try {
      String state = request.getParameter("state");
      if (state == null || state.isBlank()) {
        throw new IllegalArgumentException("State is missing in query params");
      }
      String authClientId = StateUtils.decode(state);

      AuthClient authClient = authClientRepository
        .findOne(AuthClientSpecification.byClientId(authClientId))
        .orElseThrow(() ->
          new IllegalArgumentException(AuthErrorKeys.CLIENT_INVALID.toString())
        );

      OAuth2UserSession oAuth2UserSession =
        (OAuth2UserSession) authentication.getPrincipal();
      CodeResponse codeResponse = authCodeGeneratorProvider.provide(
        oAuth2UserSession.getUser(),
        oAuth2UserSession.getUserTenant(),
        oAuth2UserSession.getRole(),
        authClient
      );
      response.setHeader(
        "Location",
        MessageFormat.format(
          "{0}?code={1}",
          authClient.getRedirectUrl(),
          codeResponse.getCode()
        )
      );
      response.setStatus(HttpStatus.FOUND.value());
    } catch (ClassCastException | IllegalArgumentException ex) {
      log.error(null, ex);

      ErrorDetails errorDetails = new ErrorDetails(
        LocalDateTime.now(),
        AuthErrorKeys.USER_VERIFICATION_FAILED.toString(),
        request.getRequestURI()
      );
      String json = Utils
        .getObjectMapperInstance()
        .writeValueAsString(errorDetails);

      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(json);
    }
  }
}
