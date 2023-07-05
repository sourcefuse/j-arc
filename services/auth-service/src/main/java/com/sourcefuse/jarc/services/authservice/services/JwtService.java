package com.sourcefuse.jarc.services.authservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.authlib.providers.JwtTokenDecryptProvider;
import com.sourcefuse.jarc.core.exception.CommonRuntimeException;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.authservice.dtos.AuthTokenRequest;
import com.sourcefuse.jarc.services.authservice.dtos.JWTAuthResponse;
import com.sourcefuse.jarc.services.authservice.dtos.RefreshTokenDTO;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthenticateErrorKeys;
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
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import lombok.RequiredArgsConstructor;
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
  private final JwtTokenDecryptProvider jwtTokenDecryptProvider;
  private final JwtRedisService jwtRedisService;

  public JWTAuthResponse getTokenByCode(AuthTokenRequest authTokenRequest) {
    JwtTokenRedis jwtTokenObject = jwtRedisService.getJwtTokenRedis(
      authTokenRequest.getCode()
    );
    CurrentUser currentUser = jwtTokenDecryptProvider.getUserDetails(
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

    User user = userRepository
      .findById(currentUser.getId())
      .orElseThrow(this::throwUserDoesNotExistException);

    UserTenant userTenant = userTenantRepository
      .findById(currentUser.getUserTenantId())
      .orElseThrow(this::throwUserDoesNotExistException);

    Role role = roleRepository
      .findById(currentUser.getRoleId())
      .orElseThrow(this::throwUserDoesNotExistException);

    return this.jwtTokenProvider.createJwt(user, userTenant, role, authClient);
  }

  public JWTAuthResponse refreshToken(
    String authorizationHeader,
    RefreshTokenDTO refreshTokenDTO
  ) {
    String accessToken = authorizationHeader.split(" ")[1];
    RefreshTokenRedis refreshTokenRedis = jwtRedisService.getRefreshTokenRedis(
      refreshTokenDTO.getRefreshToken().toString(),
      accessToken
    );
    AuthClient client = authClientRepository
      .findOne(
        AuthClientSpecification.byClientId(refreshTokenRedis.getClientId())
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

    jwtRedisService.setWithTtl(
      accessToken,
      new RevokedTokenRedis(accessToken, accessToken),
      client.getRefreshTokenExpiration()
    );
    this.jwtRedisService.deleteRedisKeyById(refreshTokenRedis.getId());
    return this.createJwt(refreshTokenRedis, client, currentUser);
  }

  JWTAuthResponse createJwt(
    RefreshTokenRedis refreshTokenRedis,
    AuthClient client,
    CurrentUser currentUser
  ) {
    User user = userRepository
      .findById(refreshTokenRedis.getUserId())
      .orElseThrow(this::throwUserDoesNotExistException);

    UserTenant userTenant = userTenantRepository
      .findById(currentUser.getUserTenantId())
      .orElseThrow(this::throwUserDoesNotExistException);

    Role role = roleRepository
      .findById(currentUser.getRoleId())
      .orElseThrow(this::throwUserDoesNotExistException);

    return jwtTokenProvider.createJwt(user, userTenant, role, client);
  }

  HttpServerErrorException throwUserDoesNotExistException() {
    return new HttpServerErrorException(
      HttpStatus.UNAUTHORIZED,
      AuthenticateErrorKeys.USER_DOES_NOT_EXISTS.toString()
    );
  }
}
