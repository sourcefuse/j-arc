package com.sourcefuse.jarc.authlib.api.security.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sourcefuse.jarc.authlib.api.security.config.CrossOriginEmbedderPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;

public class CrossOriginEmbedderPolicy extends OncePerRequestFilter {

  private final String headerValue;

  public CrossOriginEmbedderPolicy() {
    headerValue = getHeaderValueFromOptions(null);
  }

  public CrossOriginEmbedderPolicy(CrossOriginEmbedderPolicyConfig options) {
    headerValue = getHeaderValueFromOptions(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("Cross-Origin-Embedder-Policy", headerValue);
    filterChain.doFilter(request, response);
  }

  private static String getHeaderValueFromOptions(
    CrossOriginEmbedderPolicyConfig options
  ) {
    String policy = "require-corp";
    if (options != null) {
      policy = options.getPolicy().toLowerCase();
    }
    if (
      MiddlewareConstants.ALLOWED_CROSS_ORIGIN_EMBEDDER_POLICIES.contains(
        policy
      )
    ) {
      return policy;
    } else {
      throw new RuntimeException(
        "Cross-Origin-Embedder-Policy does not support the " + policy + "policy"
      );
    }
  }
}
