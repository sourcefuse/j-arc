package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;

public interface UserTenantRepository
    extends SoftDeletesRepository<UserTenant, UUID> {
}
