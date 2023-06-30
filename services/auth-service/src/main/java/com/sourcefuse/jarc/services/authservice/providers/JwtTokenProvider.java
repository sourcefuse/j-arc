package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.authlib.Utils;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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

  public JWTAuthResponse createJwt(
    User user,
    UserTenant userTenant,
    Role role,
    AuthClient authClient
  ) {
    try {
      CurrentUser currentUser =
        this.jwtPayloadProvider.provide(user, userTenant, role);
      String accessToken = Utils.generateAccessToken(
        jwtSecret,
        jwtExpirationDate,
        currentUser
      );
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
          TimeUnit.SECONDS
        );
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
        "Error while generating JWT token"
      );
    }
  }
}
