package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserTenantRepository extends CrudRepository<UserTenant, UUID> {
  Optional<UserTenant> findUserTenantByUserId(UUID userId);

  @Query(
    "SELECT ut from UserTenant ut where userId=:userId AND tenantId=:tenantId AND status NOT IN :statuses"
  )
  Optional<UserTenant> findUserBy(
    UUID userId,
    UUID tenantId,
    @Param("statuses") List<UserStatus> statuses
  );

  @Query(
    "SELECT ut from UserTenant ut where userId=:userId AND tenantId=:tenantId"
  )
  Optional<UserTenant> findUserBy(UUID userId, UUID tenantId);
}
