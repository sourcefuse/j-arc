package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTenantRepository
  extends SoftDeletesRepository<UserTenant, UUID> {

  @Query(
    "SELECT ut from UserTenant ut where userId=:userId AND tenantId=:tenantId"
  )
  Optional<UserTenant> findUserBy(UUID userId, UUID tenantId);
}
