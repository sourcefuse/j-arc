package com.sourcefuse.jarc.services.authservice.providers;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.sourcefuse.jarc.core.constants.AuthConstants;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.authservice.Utils;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.security.CustomJwtBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app-jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  private final JwtPayloadProvider jwtPayloadProvider;
  private final RedisTemplate<String, Object> redisTemplate;

  private String generateToken(CurrentUser currentUser) {
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
    Claims claims = Jwts
        .claims()
        .setSubject(currentUser.getUsername());
    claims.put(AuthConstants.CURRENT_USER_KEY, currentUser);
    return new CustomJwtBuilder()
        .setClaims(claims)
        .setIssuedAt(currentDate)
        .setExpiration(expireDate)
        .signWith(key())
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public JWTAuthResponse createJwt(
      User user,
      UserTenant userTenant,
      Role role,
      AuthClient authClient) {
    try {
      CurrentUser currentUser = this.jwtPayloadProvider.provide(user, userTenant, role);
      String accessToken = this.generateToken(currentUser);
      String refreshToken = UUID.randomUUID().toString();

      RefreshTokenRedis refreshTokenRedis = new RefreshTokenRedis();

      refreshTokenRedis.setClientId(authClient.getClientId());
      refreshTokenRedis.setUserId(currentUser.getId());
      refreshTokenRedis.setUsername(currentUser.getUsername());
      refreshTokenRedis.setExternalAuthToken(accessToken);
      refreshTokenRedis.setExternalRefreshToken(refreshToken);
      refreshTokenRedis.setId(refreshToken);
      redisTemplate
          .opsForValue()
          .set(
              refreshToken,
              refreshTokenRedis,
              authClient.getRefreshTokenExpiration(),
              TimeUnit.SECONDS);
      JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
      jwtAuthResponse.setAccessToken(accessToken);
      jwtAuthResponse.setTokenType("Bearer");
      jwtAuthResponse.setExpires(new Date().getTime() + jwtExpirationDate);
      jwtAuthResponse.setRefreshToken(refreshToken);
      return jwtAuthResponse;
    } catch (Exception e) {
      log.error(null, e);
      throw new CommonRuntimeException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Error while generating JWT token");
    }
  }

}
