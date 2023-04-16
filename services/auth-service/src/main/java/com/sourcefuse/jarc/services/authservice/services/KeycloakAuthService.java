package com.sourcefuse.jarc.services.authservice.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.payload.CodeResponse;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakAuthResponse;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakUserDTO;
import com.sourcefuse.jarc.services.authservice.providers.AuthCodeGeneratorProvider;
import com.sourcefuse.jarc.services.authservice.providers.KeycloakPostVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.KeycloakPreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.KeycloakSignupProvider;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class KeycloakAuthService {

  private final UserRepository userRepository;
  private final UserCredentialRepository userCredentialRepository;
  private final KeycloakFacadeService keycloakFacadeService;
  private final KeycloakPreVerifyProvider keycloakPreVerifyProvider;
  private final KeycloakPostVerifyProvider keycloakPostVerifyProvider;
  private final KeycloakSignupProvider keycloakSignupProvider;
  private final AuthCodeGeneratorProvider authCodeGeneratorProvider;

  public CodeResponse login(String code, AuthClient authClient) {
    KeycloakAuthResponse keycloakAuthResponse = this.keycloakFacadeService.keycloakAuthByCode(code);
    KeycloakUserDTO keycloakUserDTO = this.keycloakFacadeService.getKeycloakUserProfile(
        keycloakAuthResponse.getAccess_token());
    String usernameOrEmail = keycloakUserDTO.getEmail();
    Optional<User> user = this.userRepository.findFirstUserByUsernameOrEmail(usernameOrEmail);

    user = this.keycloakPreVerifyProvider.provide(user, keycloakUserDTO);
    if (user.isEmpty()) {
      user = this.keycloakSignupProvider.provide(keycloakUserDTO);
      if (user.isEmpty()) {
        throw new HttpServerErrorException(
            HttpStatus.UNAUTHORIZED,
            AuthErrorKeys.UserVerificationFailed.label);
      }
    }
    Optional<UserCredential> userCredential = this.userCredentialRepository.findByUserId(user.get().getId());
    if (userCredential.isEmpty() ||
        !userCredential.get().getAuthProvider().equals("keycloak") ||
        (!userCredential.get().getAuthId().equals(keycloakUserDTO.getPreferred_username()))) {
      throw new HttpServerErrorException(
          HttpStatus.UNAUTHORIZED,
          AuthErrorKeys.UserVerificationFailed.label);
    }

    this.keycloakPostVerifyProvider.provide(keycloakUserDTO, user);
    return authCodeGeneratorProvider.provide(user.get(), authClient);
  }
}
