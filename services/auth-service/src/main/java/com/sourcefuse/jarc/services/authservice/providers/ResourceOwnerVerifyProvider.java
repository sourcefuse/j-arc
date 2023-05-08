package com.sourcefuse.jarc.services.authservice.providers;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthenticateErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.payload.LoginDto;
import com.sourcefuse.jarc.services.authservice.payload.UserVerificationDTO;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.services.AuthService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ResourceOwnerVerifyProvider {

  private UserRepository userRepository;
  private UserTenantRepository userTenantRepository;
  private AuthClientRepository authClientRepository;
  private AuthService authService;

  public UserVerificationDTO provide(LoginDto loginDto) throws HttpServerErrorException {
    Optional<User> user;

    try {
      user = this.authService.verifyPassword(loginDto.getUsername(), loginDto.getPassword());
    } catch (Exception error) {
      user = this.userRepository.findUserByUsername(loginDto.getUsername());
      if (user.isEmpty()) {
        throw new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.INVALID_CREDENTIALS.label);
      }
    }
    this.userTenantRepository.findUserBy(
        user.get().getId(),
        user.get().getDefaultTenantId(),
        Arrays.asList(UserStatus.REJECTED, UserStatus.INACTIVE))
        .orElseThrow(() -> new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthenticateErrorKeys.USER_INACTIVE.label));

    AuthClient client = this.authClientRepository.findAuthClientByClientId(loginDto.getClientId())
        .orElseThrow(() -> new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.CLIENT_INVALID.label));

    if (!user.get().getAuthClientIds().contains(client.getId())) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.CLIENT_INVALID.label);
    } else if (!Objects.equals(client.getClientSecret(), loginDto.getClientSecret())) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.CLIENT_VERIFICATION_FAILED.label);
    } else{
      // for sonar
    }

    return new UserVerificationDTO(client, user.get());
  }
}
