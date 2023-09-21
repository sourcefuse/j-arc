package com.sourcefuse.jarc.services.authservice.oauth2.request.filters;

import com.sourcefuse.jarc.authlib.utils.Utils;
import com.sourcefuse.jarc.core.dtos.ErrorDetails;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.utils.StateUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class OAuth2AuthorizationRequestFilter extends OncePerRequestFilter {

  private final AntPathRequestMatcher authorizationRequestMatcher;

  public OAuth2AuthorizationRequestFilter(String authorizationRequestBaseUri) {
    authorizationRequestMatcher =
      new AntPathRequestMatcher(
        authorizationRequestBaseUri + "/{registrationId}"
      );
  }

  public OAuth2AuthorizationRequestFilter() {
    authorizationRequestMatcher =
      new AntPathRequestMatcher(
        OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI +
        "/{registrationId}"
      );
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String clientId = request.getParameter(StateUtils.clientIdParamKey);
    if (
      authorizationRequestMatcher.matches(request) &&
      (clientId == null || clientId.isBlank())
    ) {
      String errorMessage =
        "clientId is missing in the query parameters of the request Query Parameters:" +
        request.getQueryString();
      ErrorDetails errorDetails = new ErrorDetails(
        LocalDateTime.now(),
        errorMessage,
        request.getRequestURI()
      );
      String json = Utils
        .getObjectMapperInstance()
        .writeValueAsString(errorDetails);

      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.getWriter().write(json);
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
