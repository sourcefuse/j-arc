package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.User;

public interface UserRepository extends SoftDeletesRepository<User, UUID> {
}
