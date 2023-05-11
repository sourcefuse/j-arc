package com.sourcefuse.jarc.services.authservice.security;

import com.sourcefuse.jarc.services.authservice.providers.JwtTokenProvider;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static String bearerPrefix = "Bearer ";
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String token = getTokenFromRequest(request);
    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
      CurrentUser user = jwtTokenProvider.getUserDetails(token);
      List<GrantedAuthority> listAuthorities = new ArrayList<>();
      for (String permission : user.getRole().getPermissions()) {
        listAuthorities.add(new SimpleGrantedAuthority(permission));
      }
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        user,
        null,
        listAuthorities
      );

      authenticationToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
      );

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    filterChain.doFilter(request, response);
  }

  private static String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(bearerPrefix.length(), bearerToken.length());
    }

    return null;
  }
}
