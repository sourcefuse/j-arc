package com.sourcefuse.jarc.authlib.api.security.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

// order for this filter should be first otherwise it wont remove the header
public class XPoweredBy extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    filterChain.doFilter(
      request,
      new HttpServletResponseWrapper(response) {
        @Override
        public void setHeader(String name, String value) {
          if (!"X-Powered-By".equalsIgnoreCase(name)) {
            super.setHeader(name, value);
          }
        }
      }
    );
  }
}
