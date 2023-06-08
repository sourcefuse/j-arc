package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.dtos.keycloak.KeycloakUserDTO;
import com.sourcefuse.jarc.services.authservice.enums.AuthErrorKeys;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import com.sourcefuse.jarc.services.authservice.specifications.UserTenantSpecification;
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
      !user.getFirstName().equals(keycloakUserDTO.getGivenName()) ||
      !user.getLastName().equals(keycloakUserDTO.getFamilyName()) ||
      !user.getUsername().equals(keycloakUserDTO.getPreferredUsername()) ||
      !user.getEmail().equals(keycloakUserDTO.getEmail())
    ) {
      user.setUsername(keycloakUserDTO.getPreferredUsername());
      user.setFirstName(keycloakUserDTO.getGivenName());
      user.setLastName(keycloakUserDTO.getFamilyName());
      user.setEmail(keycloakUserDTO.getEmail());
      this.userRepository.save(user);
    }
    Optional<UserTenant> userTenant =
      this.userTenantRepository.findOne(
          UserTenantSpecification.byUserId(user.getId())
        );
    if (userTenant.isEmpty()) {
      throw new HttpServerErrorException(
        HttpStatus.UNAUTHORIZED,
        AuthErrorKeys.INVALID_CREDENTIALS.toString()
      );
    }
    if (userTenant.get().getStatus() == UserStatus.REGISTERED) {
      userTenant.get().setStatus(UserStatus.ACTIVE);
      this.userTenantRepository.save(userTenant.get());
    }
    return Optional.of(user);
  }
}
