package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import java.util.UUID;

public interface UserTenantRepository
  extends SoftDeletesRepository<UserTenant, UUID> {}
