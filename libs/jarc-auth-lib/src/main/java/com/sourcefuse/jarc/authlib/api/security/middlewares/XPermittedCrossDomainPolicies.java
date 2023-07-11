package com.sourcefuse.jarc.authlib.api.security.middlewares;

import com.sourcefuse.jarc.authlib.api.security.config.XPermittedCrossDomainPoliciesConfig;
import com.sourcefuse.jarc.authlib.api.security.types.MiddlewareConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class XPermittedCrossDomainPolicies extends OncePerRequestFilter {

  private final String headerValue;

  public XPermittedCrossDomainPolicies() {
    headerValue = getHeaderValueFromOptions(null);
  }

  public XPermittedCrossDomainPolicies(
    XPermittedCrossDomainPoliciesConfig options
  ) {
    headerValue = getHeaderValueFromOptions(options);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("X-Powered-By", "ASP.NET");
    response.setHeader("X-Permitted-Cross-Domain-Policies", headerValue);
    filterChain.doFilter(request, response);
  }

  private static String getHeaderValueFromOptions(
    XPermittedCrossDomainPoliciesConfig options
  ) {
    String permittedPolicies = "none";
    if (options != null) {
      permittedPolicies = options.getPermittedPolicies().toLowerCase();
    }
    if (
      MiddlewareConstants.ALLOWED_PERMITTED_CROSS_DOMAIN_POLICIES.contains(
        permittedPolicies
      )
    ) {
      return permittedPolicies;
    } else {
      throw new IllegalArgumentException(
        "X-Permitted-Cross-Domain-Policies does not support " +
        permittedPolicies
      );
    }
  }
}
