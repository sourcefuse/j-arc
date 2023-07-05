package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.Role;
import java.util.UUID;

public interface RoleRepository extends SoftDeletesRepository<Role, UUID> {}
