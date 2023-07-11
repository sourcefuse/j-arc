package com.sourcefuse.jarc.authlib.api.security.middlewares;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginOpenerPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class CrossOriginOpenerPolicy extends OncePerRequestFilter {

  private final String headerValue;

  public CrossOriginOpenerPolicy() {
    headerValue = getHeaderValueFromOptions(null);
  }

  public CrossOriginOpenerPolicy(CrossOriginOpenerPolicyConfig options) {
    headerValue = getHeaderValueFromOptions(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("Cross-Origin-Opener-Policy", headerValue);
    filterChain.doFilter(request, response);
  }

  private static String getHeaderValueFromOptions(
    CrossOriginOpenerPolicyConfig options
  ) {
    String policy = "same-origin";
    if (options != null) {
      policy = options.getPolicy().toLowerCase();
    }
    if (
      MiddlewareConstants.ALLOWED_CROSS_ORIGIN_OPENER_POLICIES.contains(policy)
    ) {
      return policy;
    } else {
      throw new IllegalArgumentException(
        "Cross-Origin-Opener-Policy does not support the " + policy + "policy"
      );
    }
  }
}
