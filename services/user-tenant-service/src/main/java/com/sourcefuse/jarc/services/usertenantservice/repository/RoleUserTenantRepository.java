package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleUserTenantRepository extends JpaRepository<UserTenant, UUID> {
    List<UserTenant> findUserTenantsByRoleId(UUID id);

    Long deleteByRoleId(UUID id);
}
