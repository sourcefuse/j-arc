package com.sourcefuse.jarc.core.test.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.core.test.models.User;
import java.util.UUID;

public interface UserRepository extends SoftDeletesRepository<User, UUID> {}
