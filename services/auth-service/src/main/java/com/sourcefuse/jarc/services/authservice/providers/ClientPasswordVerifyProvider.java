package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClientPasswordVerifyProvider {

  private final AuthClientRepository authClientRepository;

  public AuthClient value(String clientId, String clientSecret) {
    return this.authClientRepository.findAuthClientByClientIdAndClientSecret(
        clientId,
        clientSecret
      );
  }
}
