package com.sourcefuse.jarc.authlib.api.security.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sourcefuse.jarc.authlib.api.security.config.StrictTransportSecurityConfig;
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;

public class StrictTransportSecurity extends OncePerRequestFilter {

  private final String headerValue;

  public StrictTransportSecurity() {
    headerValue = getHeaderValueFromOptions(null);
  }

  public StrictTransportSecurity(StrictTransportSecurityConfig options) {
    headerValue = getHeaderValueFromOptions(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("Strict-Transport-Security", headerValue);
    filterChain.doFilter(request, response);
  }

  private static Double parseMaxAge(Double maxAge) {
    if (maxAge == null) {
      maxAge = MiddlewareConstants.DEFAULT_MAX_AGE;
    }
    if (maxAge >= 0 && Double.isFinite(maxAge)) {
      return Math.floor(maxAge);
    } else {
      throw new IllegalArgumentException(
        "Strict-Transport-Security: " +
        maxAge +
        " is not a valid value for maxAge. Please choose a positive integer."
      );
    }
  }

  private static String getHeaderValueFromOptions(
    StrictTransportSecurityConfig options
  ) {
    if (options == null) {
      options = new StrictTransportSecurityConfig();
    }
    List<String> directives = new ArrayList<>();
    directives.add("max-age=" + parseMaxAge(options.getMaxAge()));

    if (
      options.getIncludeSubDomains() == null || options.getIncludeSubDomains()
    ) {
      directives.add("includeSubDomains");
    }

    if (options.isPreload()) {
      directives.add("preload");
    }

    return String.join("; ", directives);
  }
}
