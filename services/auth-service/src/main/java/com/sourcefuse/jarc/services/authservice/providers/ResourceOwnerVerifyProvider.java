package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.dtos.LoginDto;
import com.sourcefuse.jarc.services.authservice.dtos.UserVerificationDTO;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthenticateErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.services.AuthService;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@AllArgsConstructor
@Service
@Slf4j
public class ResourceOwnerVerifyProvider {

  private UserRepository userRepository;
  private UserTenantRepository userTenantRepository;
  private AuthClientRepository authClientRepository;
  private AuthService authService;

  public UserVerificationDTO provide(LoginDto loginDto)
    throws HttpServerErrorException {
    Optional<User> user;
    try {
      user =
        this.authService.verifyPassword(
            loginDto.getUsername(),
            loginDto.getPassword()
          );
    } catch (Exception error) {
      log.error(null, error);
      user =
        userRepository.findOne(
          UserSpecification.byUsername(loginDto.getUsername())
        );
    }
    if (user.isEmpty()) {
      throw throwUnauthorizedException(AuthErrorKeys.INVALID_CREDENTIALS);
    }
    Optional<UserTenant> userTenant =
      this.userTenantRepository.findOne(
          UserTenantSpecification.byUserIdTenantIdAndStatusNotIn(
            user.get().getId(),
            user.get().getDefaultTenantId(),
            Arrays.asList(UserStatus.REJECTED, UserStatus.INACTIVE)
          )
        );
    if (userTenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthenticateErrorKeys.USER_INACTIVE.toString()
      );
    }
    AuthClient client =
      this.authClientRepository.findOne(
          AuthClientSpecification.byClientId(loginDto.getClientId())
        )
        .orElseThrow(() ->
          throwUnauthorizedException(AuthErrorKeys.CLIENT_INVALID)
        );
    if (!user.get().getAuthClientIds().contains(client.getId())) {
      throw throwUnauthorizedException(AuthErrorKeys.CLIENT_INVALID);
    } else if (
      !Objects.equals(client.getClientSecret(), loginDto.getClientSecret())
    ) {
      throw throwUnauthorizedException(AuthErrorKeys.CLIENT_INVALID);
    } else {
      // for sonar
    }
    return new UserVerificationDTO(client, user.get());
  }

  HttpServerErrorException throwUnauthorizedException(AuthErrorKeys errorKeys) {
    throw new HttpServerErrorException(
      HttpStatus.UNAUTHORIZED,
      errorKeys.toString()
    );
  }
}
