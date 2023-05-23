package com.sourcefuse.jarc.services.authservice.services;

import com.sourcefuse.jarc.services.authservice.dtos.CodeResponse;
import com.sourcefuse.jarc.services.authservice.dtos.keycloak.KeycloakAuthResponse;
import com.sourcefuse.jarc.services.authservice.dtos.keycloak.KeycloakUserDTO;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.providers.AuthCodeGeneratorProvider;
import com.sourcefuse.jarc.services.authservice.providers.KeycloakPostVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.KeycloakPreVerifyProvider;
import com.sourcefuse.jarc.services.authservice.providers.KeycloakSignupProvider;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.UserCredentialSpecification;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@AllArgsConstructor
@Component
public class KeycloakAuthService {

  private final UserRepository userRepository;
  private final UserCredentialRepository userCredentialRepository;
  private final KeycloakFacadeService keycloakFacadeService;
  private final KeycloakPreVerifyProvider keycloakPreVerifyProvider;
  private final KeycloakPostVerifyProvider keycloakPostVerifyProvider;
  private final KeycloakSignupProvider keycloakSignupProvider;
  private final AuthCodeGeneratorProvider authCodeGeneratorProvider;
  private final UserTenantRepository userTenantRepository;
  private final RoleRepository roleRepository;

  public CodeResponse login(String code, AuthClient authClient) {
    KeycloakAuthResponse keycloakAuthResponse =
      this.keycloakFacadeService.keycloakAuthByCode(code);
    KeycloakUserDTO keycloakUserDTO =
      this.keycloakFacadeService.getKeycloakUserProfile(
          keycloakAuthResponse.getAccessToken()
        );
    String usernameOrEmail = keycloakUserDTO.getEmail();
    User user = this.getUserBy(usernameOrEmail, keycloakUserDTO);
    UserCredential userCredential =
      this.userCredentialRepository.findOne(
          UserCredentialSpecification.byUserId(user.getId())
        )
        .orElseThrow(this::throwUserVerificationFailed);
    if (
      !userCredential
        .getAuthProvider()
        .equals(AuthProvider.KEYCLOAK.toString()) ||
      (
        !userCredential
          .getAuthId()
          .equals(keycloakUserDTO.getPreferredUsername())
      )
    ) {
      throw throwUserVerificationFailed();
    }
    this.keycloakPostVerifyProvider.provide(keycloakUserDTO, user);
    UserTenant userTenant =
      this.userTenantRepository.findOne(
          UserTenantSpecification.byUserId(user.getId())
        )
        .orElseThrow(this::throwUserVerificationFailed);

    Role role =
      this.roleRepository.findById(userTenant.getRoleId())
        .orElseThrow(this::throwUserVerificationFailed);

    return authCodeGeneratorProvider.provide(
      user,
      userTenant,
      role,
      authClient
    );
  }

  User getUserBy(String usernameOrEmail, KeycloakUserDTO keycloakUserDTO) {
    Optional<User> user =
      this.userRepository.findFirstUserByUsernameOrEmail(usernameOrEmail);
    if (user.isPresent()) {
      user =
        this.keycloakPreVerifyProvider.provide(user.get(), keycloakUserDTO);
    } else {
      user = this.keycloakSignupProvider.provide(keycloakUserDTO);
    }
    if (user.isEmpty()) {
      throw this.throwUserVerificationFailed();
    }
    return user.get();
  }

  HttpServerErrorException throwUserVerificationFailed() {
    return new HttpServerErrorException(
      HttpStatus.UNAUTHORIZED,
      AuthErrorKeys.USER_VERIFICATION_FAILED.toString()
    );
  }
}
