package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleUserTenantRepository
  extends SoftDeletesRepository<UserTenant, UUID> {
  List<UserTenant> findUserTenantsByRoleId(UUID id);

  Long deleteByRoleId(UUID id);
}
