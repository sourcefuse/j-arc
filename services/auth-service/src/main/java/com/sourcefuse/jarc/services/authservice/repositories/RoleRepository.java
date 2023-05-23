package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.Role;

public interface RoleRepository extends SoftDeletesRepository<Role, UUID> {}
