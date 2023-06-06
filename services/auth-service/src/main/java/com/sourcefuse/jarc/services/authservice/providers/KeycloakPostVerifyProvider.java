package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.dtos.keycloak.KeycloakUserDTO;
import com.sourcefuse.jarc.services.authservice.models.User;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class KeycloakPostVerifyProvider {

  public Optional<User> provide(KeycloakUserDTO keycloakUserDTO, User user) {
    return Optional.empty();
  }
}
