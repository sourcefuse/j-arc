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
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
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
      // TODO
      // const otp: Otp = await this.otpRepository.get(username);
      // if (!otp || otp.otp !== password) {
      // throw new HttpErrors.Unauthorized(AuthErrorKeys.InvalidCredentials);
      // }
      user = this.userRepository.findUserByUsername(loginDto.getUsername());
      if (user.isEmpty()) {
        throw new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.InvalidCredentials.label);
      }
    }
    this.userTenantRepository.findUserBy(
        user.get().getId(),
        user.get().getDefaultTenantId(),
        Arrays.asList(UserStatus.REJECTED, UserStatus.INACTIVE))
        .orElseThrow(() -> new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthenticateErrorKeys.UserInactive.label));

    AuthClient client = this.authClientRepository.findAuthClientByClientId(loginDto.getClient_id())
        .orElseThrow(() -> new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.ClientInvalid.label));

    if (!user.get().getAuthClientIds().contains(client.getId())) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.ClientInvalid.label);
    } else if (!Objects.equals(client.getClientSecret(), loginDto.getClient_secret())) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.ClientVerificationFailed.label);
    }

    return new UserVerificationDTO(client, user.get());
  }
}
