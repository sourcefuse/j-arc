package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AuthClientRepository
  extends CrudRepository<AuthClient, String> {
  Optional<AuthClient> findAuthClientByClientId(String clientId);

  AuthClient findAuthClientByClientIdAndClientSecret(
    String clientId,
    String clientSecret
  );

  @Query("SELECT ac from AuthClient ac where ac.clientId IN :allowedClients ")
  ArrayList<AuthClient> findByAllowedClients(List<String> allowedClients);
}
