package com.sourcefuse.jarc.authlib.api.security.middlewares;

import com.sourcefuse.jarc.authlib.api.security.config.XFrameOptionsConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class XFrameOptions extends OncePerRequestFilter {

  private final String headerValue;

  public XFrameOptions() {
    headerValue = getHeaderValueFromOptions(null);
  }

  public XFrameOptions(XFrameOptionsConfig options) {
    headerValue = getHeaderValueFromOptions(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("X-Frame-Options", headerValue);
    filterChain.doFilter(request, response);
  }

  private static String getHeaderValueFromOptions(XFrameOptionsConfig options) {
    String normalizedAction = "sameorigin";
    if (options != null) {
      normalizedAction = options.getAction();
    }
    switch (normalizedAction.toUpperCase()) {
      case "SAME-ORIGIN":
      case "DENY":
      case "SAMEORIGIN":
        return "SAMEORIGIN";
      default:
        throw new IllegalArgumentException(
          "X-Frame-Options received an invalid action " + normalizedAction
        );
    }
  }
}
