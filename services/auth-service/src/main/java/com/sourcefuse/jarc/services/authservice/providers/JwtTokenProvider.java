package com.sourcefuse.jarc.services.authservice.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.authservice.Constants;
import com.sourcefuse.jarc.services.authservice.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.payload.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
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
      .setSubject(currentUser.getUser().getUsername());
    claims.put(Constants.CURRENT_USER_KEY, currentUser);
    return Jwts
      .builder()
      .setClaims(claims)
      .setIssuedAt(new Date())
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
    AuthClient authClient
  ) {
    CurrentUser currentUser =
      this.jwtPayloadProvider.provide(user, userTenant, role);
    String accessToken = this.generateToken(currentUser);
    String refreshToken = UUID.randomUUID().toString();

    RefreshTokenRedis refreshTokenRedis = new RefreshTokenRedis();

    refreshTokenRedis.setClientId(authClient.getClientId());
    refreshTokenRedis.setUserId(user.getId());
    refreshTokenRedis.setUsername(user.getUsername());
    refreshTokenRedis.setExternalAuthToken(accessToken);
    refreshTokenRedis.setExternalRefreshToken(refreshToken);
    refreshTokenRedis.setId(refreshToken);
    redisTemplate
      .opsForValue()
      .set(
        refreshToken,
        refreshTokenRedis,
        authClient.getRefreshTokenExpiration(),
        TimeUnit.SECONDS
      );
    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccessToken(accessToken);
    jwtAuthResponse.setTokenType("Bearer");
    jwtAuthResponse.setExpires(new Date().getTime() + jwtExpirationDate);
    jwtAuthResponse.setRefreshToken(refreshToken);
    return jwtAuthResponse;
  }

  public CurrentUser getUserDetails(String token) {
    Claims claims = Jwts
      .parserBuilder()
      .setSigningKey(key())
      .build()
      .parseClaimsJws(token)
      .getBody();
    Object userObject = claims.get(Constants.CURRENT_USER_KEY);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(userObject, CurrentUser.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
      return true;
    } catch (MalformedJwtException ex) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Invalid JWT token"
      );
    } catch (ExpiredJwtException ex) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Expired JWT token"
      );
    } catch (UnsupportedJwtException ex) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "Unsupported JWT token"
      );
    } catch (IllegalArgumentException ex) {
      throw new CommonRuntimeException(
        HttpStatus.BAD_REQUEST,
        "JWT claims string is empty."
      );
    }
  }
}
