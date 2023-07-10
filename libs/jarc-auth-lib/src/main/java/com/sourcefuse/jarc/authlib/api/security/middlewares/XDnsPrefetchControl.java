package com.sourcefuse.jarc.authlib.api.security.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sourcefuse.jarc.authlib.api.security.config.XDnsPrefetchControlConfig;

public class XDnsPrefetchControl extends OncePerRequestFilter {

  private final String headerValue;

  public XDnsPrefetchControl() {
    headerValue = "off";
  }

  public XDnsPrefetchControl(XDnsPrefetchControlConfig options) {
    headerValue = options.isAllow() ? "on" : "off";
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    response.setHeader("X-DNS-Prefetch-Control", headerValue);
    filterChain.doFilter(request, response);
  }
}
