package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.UserCredential;

public interface UserCredentialRepository
    extends SoftDeletesRepository<UserCredential, String> {
}
