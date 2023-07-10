package com.sourcefuse.jarc.authlib.api.security.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sourcefuse.jarc.authlib.api.security.config.ReferrerPolicyConfig;
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;

public class ReferrerPolicy extends OncePerRequestFilter {

  private final String headerValue;

  public ReferrerPolicy() {
    headerValue = getHeaderValueFromOptions(null);
  }

  public ReferrerPolicy(ReferrerPolicyConfig options) {
    headerValue = getHeaderValueFromOptions(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("Referrer-Policy", headerValue);
    filterChain.doFilter(request, response);
  }

  private static String getHeaderValueFromOptions(
    ReferrerPolicyConfig options
  ) {
    if (options == null) {
      options = new ReferrerPolicyConfig();
    }
    if (options.getPolicies() == null) {
      options.setPolicies(Set.of("no-referrer"));
    }

    if (options.getPolicies().isEmpty()) {
      throw new IllegalArgumentException(
        "Referrer-Policy received no policy tokens"
      );
    }

    Set<String> tokensSeen = new HashSet<>();
    for (String token : options.getPolicies()) {
      if (!MiddlewareConstants.ALLOWED_REFFERER_POLICY_TOKENS.contains(token)) {
        throw new IllegalArgumentException(
          "Referrer-Policy received an unexpected policy token " + token
        );
      } else if (tokensSeen.contains(token)) {
        throw new IllegalArgumentException(
          "Referrer-Policy received a duplicate policy token " + token
        );
      } else {
        // for sonar
      }
      tokensSeen.add(token);
    }

    return String.join(",", options.getPolicies());
  }
}
