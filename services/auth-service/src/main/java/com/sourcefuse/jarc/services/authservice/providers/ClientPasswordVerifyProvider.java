package com.sourcefuse.jarc.services.authservice.providers;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClientPasswordVerifyProvider {

  private final AuthClientRepository authClientRepository;

  public AuthClient value(String clientId, String clientSecret) {
    return this.authClientRepository.findOne(
        AuthClientSpecification.byClientIdAndClientSecret(
          clientId,
          clientSecret
        )
      )
      .orElse(null);
  }
}
