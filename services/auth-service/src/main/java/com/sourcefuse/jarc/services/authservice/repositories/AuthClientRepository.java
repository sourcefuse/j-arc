package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;

public interface AuthClientRepository
  extends SoftDeletesRepository<AuthClient, String> {
}
