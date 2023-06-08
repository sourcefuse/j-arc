package com.sourcefuse.jarc.services.authservice.providers;

import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.repositories.AuthClientRepository;
import com.sourcefuse.jarc.services.authservice.specifications.AuthClientSpecification;

import lombok.AllArgsConstructor;

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
