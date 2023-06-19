package com.sourcefuse.jarc.authlib.providers;

import java.security.Key;
import com.sourcefuse.jarc.authlib.Utils;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.sourcefuse.jarc.core.constants.AuthConstants;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenDecryptProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public CurrentUser getUserDetails(String token) {
    try {
      Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();
      Object userObject = claims.get(AuthConstants.CURRENT_USER_KEY);
      return Utils
        .getObjectMapperInstance()
        .convertValue(userObject, CurrentUser.class);
    } catch (MalformedJwtException ex) {
      log.error(null, ex);
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Invalid JWT token"
      );
    } catch (ExpiredJwtException ex) {
      log.error(null, ex);
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Expired JWT token"
      );
    } catch (UnsupportedJwtException ex) {
      log.error(null, ex);
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Unsupported JWT token"
      );
    } catch (IllegalArgumentException ex) {
      log.error(null, ex);
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "JWT claims string is empty."
      );
    }
  }
}
