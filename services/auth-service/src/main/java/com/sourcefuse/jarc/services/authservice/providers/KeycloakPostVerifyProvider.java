package com.sourcefuse.jarc.services.authservice.providers;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.payload.keycloak.KeycloakUserDTO;

@Service
public class KeycloakPostVerifyProvider {

  public Optional<User> provide(KeycloakUserDTO keycloakUserDTO, Optional<User> user) {
    return null;
  }
}
