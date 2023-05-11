package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTenantRepository extends JpaRepository<UserTenant, UUID> {
  Optional<UserTenant> findFirstByUserIdAndTenantIdOrderByIdAsc(
    UUID userId,
    UUID tenantId
  );

  List<UserTenant> findAllByUserIdAndTenantId(UUID id, UUID tenantId);

  void deleteAllByUserIdAndTenantId(UUID id, UUID tenantId);

  UserTenant findByUserId(UUID id);
}