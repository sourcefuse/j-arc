package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakUserDTO;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@AllArgsConstructor
@Service
public class KeycloakPreVerifyProvider {

  private final UserRepository userRepository;
  private final UserTenantRepository userTenantRepository;

  public Optional<User> provide(User user, KeycloakUserDTO keycloakUserDTO) {
    if (
      !user.getFirstName().equals(keycloakUserDTO.getGiven_name()) ||
      !user.getLastName().equals(keycloakUserDTO.getFamily_name()) ||
      !user.getUsername().equals(keycloakUserDTO.getPreferred_username()) ||
      !user.getEmail().equals(keycloakUserDTO.getEmail())
    ) {
      user.setUsername(keycloakUserDTO.getPreferred_username());
      user.setFirstName(keycloakUserDTO.getGiven_name());
      user.setLastName(keycloakUserDTO.getFamily_name());
      user.setEmail(keycloakUserDTO.getEmail());
      this.userRepository.save(user);
    }
    Optional<UserTenant> userTenant =
      this.userTenantRepository.findUserTenantByUserId(user.getId());
    if (userTenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.label
      );
    }
    // TODO role assignment pending to be updated
    if (userTenant.get().getStatus() == UserStatus.REGISTERED) {
      userTenant.get().setStatus(UserStatus.ACTIVE);
      this.userTenantRepository.save(userTenant.get());
      // await this.userCredsRepo.updateAll(
      // {
      // authId: profile.username,
      // authProvider: 'keycloak',
      // },
      // {
      // and: [
      // {userId: user.id as string},
      // {or: [{authProvider: 'keycloak'}, {authProvider: 'internal'}]},
      // ],
      // },
      // );
    }
    return Optional.of(user);
  }
}
