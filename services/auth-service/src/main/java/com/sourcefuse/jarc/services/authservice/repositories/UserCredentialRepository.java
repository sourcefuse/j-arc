package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.Optional;
import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;

public interface UserCredentialRepository
  extends SoftDeletesRepository<UserCredential, String> {

  Optional<UserCredential> findByAuthIdAndAuthProvider(
    String authId,
    String authProvider
  );
}
