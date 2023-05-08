package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.Tenant;

public interface TenantRepository
        extends SoftDeletesRepository<Tenant, String> {
}
