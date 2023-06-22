package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantConfigRepository
  extends SoftDeletesRepository<TenantConfig, UUID> {}
