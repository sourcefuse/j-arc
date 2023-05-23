package com.sourcefuse.jarc.services.authservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.authservice.dtos.AuthTokenRequest;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.dtos.RefreshTokenDTO;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthenticateErrorKeys;
import com.sourcefuse.jarc.services.authservice.exception.CommonRuntimeException;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.JwtTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.RefreshTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.RevokedTokenRedis;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.providers.JwtTokenProvider;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@RequiredArgsConstructor
@Service
public class JwtService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuthClientRepository authClientRepository;
  private final UserTenantRepository userTenantRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, Object> redisTemplate;

  public JWTAuthResponse getTokenByCode(AuthTokenRequest authTokenRequest) {
    JwtTokenRedis jwtTokenObject = 
    (JwtTokenRedis) this.redisTemplate.opsForValue()
      .get(authTokenRequest.getCode());
    if (jwtTokenObject == null) {
      throw new CommonRuntimeException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.CLIENT_INVALID.toString()
      );
    }
    CurrentUser currentUser = jwtTokenProvider.getUserDetails(
      jwtTokenObject.getToken()
    );
    AuthClient authClient =
      this.authClientRepository.findOne(
          AuthClientSpecification.byClientId(authTokenRequest.getClientId())
        )
        .orElseThrow(() ->
          new CommonRuntimeException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.CLIENT_INVALID.toString()
          )
        );
    return this.jwtTokenProvider.createJwt(
        currentUser.getUser(),
        currentUser.getUserTenant(),
        currentUser.getRole(),
        authClient
      );
  }

  public JWTAuthResponse refreshToken(
    String authorizationHeader,
    RefreshTokenDTO refreshTokenDTO
  ) {
    RefreshTokenRedis refreshTokenRedis = (RefreshTokenRedis) this.redisTemplate.opsForValue()
      .get(refreshTokenDTO.getRefreshToken().toString());

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    String accessToken = authorizationHeader.split(" ")[1];

    if (
      refreshTokenRedis == null ||
      !accessToken.equals(refreshTokenRedis.getExternalAuthToken())
    ) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.TOKEN_INVALID.toString()
      );
    }
    RefreshTokenRedis refreshTokenRedis2 = objectMapper.convertValue(
      refreshTokenRedis,
      RefreshTokenRedis.class
    );
    AuthClient client = authClientRepository
      .findOne(
        AuthClientSpecification.byClientId(refreshTokenRedis2.getClientId())
      )
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.CLIENT_INVALID.toString()
        )
      );
    CurrentUser currentUser = (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
    this.setWithTtl(
        accessToken,
        new RevokedTokenRedis(accessToken, accessToken),
        client.getRefreshTokenExpiration()
      );
    this.redisTemplate.delete(refreshTokenRedis.getId());
    User user = userRepository
      .findById(refreshTokenRedis.getUserId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.toString()
        )
      );

    UserTenant userTenant = userTenantRepository
      .findById(currentUser.getUserTenant().getId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.toString()
        )
      );

    Role role = roleRepository
      .findById(currentUser.getUserTenant().getRoleId())
      .orElseThrow(() ->
        new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.toString()
        )
      );

    return jwtTokenProvider.createJwt(user, userTenant, role, client);
  }

  public void setWithTtl(String key, Object value, long ttl) {
    ValueOperations<String, Object> ops = redisTemplate.opsForValue();
    ops.set(key, value, ttl, TimeUnit.SECONDS);
  }
}
