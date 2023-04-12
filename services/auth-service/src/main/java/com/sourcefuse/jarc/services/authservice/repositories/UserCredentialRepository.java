package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.authservice.models.UserCredential;

public interface UserCredentialRepository extends CrudRepository<UserCredential, String> {
  Optional<UserCredential> findByUserId(UUID userId);

  Optional<UserCredential> findByAuthIdAndAuthProvider(
      String authId,
      String authProvider);
}
