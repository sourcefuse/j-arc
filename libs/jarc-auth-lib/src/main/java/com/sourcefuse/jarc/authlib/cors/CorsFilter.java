package com.sourcefuse.jarc.authlib.cors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class CorsFilter extends OncePerRequestFilter {

  @Value("${cors.url:#{null}}")
  String corsUrl;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    if (corsUrl != null) {
      response.setHeader("Access-Control-Allow-Origin", corsUrl);
      response.setHeader("Access-Control-Allow-Methods", "*");
      response.setHeader("Access-Control-Allow-Headers", "*");
      response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    filterChain.doFilter(request, response);
  }
}
