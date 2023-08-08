package com.sourcefuse.jarc.authlib.security;

import com.sourcefuse.jarc.authlib.providers.JwtTokenDecryptProvider;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static String bearerPrefix = "Bearer ";
  private final JwtTokenDecryptProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws IOException, ServletException {
    try {
      String token = getTokenFromRequest(request);
      if (StringUtils.hasText(token)) {
        CurrentUser user = jwtTokenProvider.getUserDetails(token);
        List<GrantedAuthority> listAuthorities = new ArrayList<>();
        for (String permission : user.getPermissions()) {
          listAuthorities.add(new SimpleGrantedAuthority(permission));
        }
        UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(user, null, listAuthorities);

        authenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder
          .getContext()
          .setAuthentication(authenticationToken);
      }

      filterChain.doFilter(request, response);
    } catch (CommonRuntimeException exception) {
      log.error(null, exception);
      response.setStatus(exception.getStatus().value());
      response.addHeader(
        HttpHeaders.CONTENT_TYPE,
        MediaType.APPLICATION_JSON_VALUE
      );
      response
        .getWriter()
        .write(
          CommonUtils.getErrorInString(
            request.getRequestURI(),
            exception.getMessage()
          )
        );
    }
  }

  private static String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(bearerPrefix.length(), bearerToken.length());
    }

    return null;
  }
}
