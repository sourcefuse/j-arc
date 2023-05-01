package com.sourcefuse.jarc.core.test.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.core.test.models.User;

public interface UserRepository extends SoftDeletesRepository<User, UUID> {}
