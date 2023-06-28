package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.User;
import java.util.UUID;

public interface UserRepository extends SoftDeletesRepository<User, UUID> {}
