package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserTenantRepository
  extends JpaRepository<UserTenant, UUID> {
  List<UserTenant> findUserTenantsByRoleId(UUID id);

  Long deleteByRoleId(UUID id);
}
