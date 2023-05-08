package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakUserDTO;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class KeycloakPostVerifyProvider {

  public Optional<User> provide(
    KeycloakUserDTO keycloakUserDTO,
    Optional<User> user
  ) {
    return null;
  }
}
