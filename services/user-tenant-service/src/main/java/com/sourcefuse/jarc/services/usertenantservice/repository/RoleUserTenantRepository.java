package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoleUserTenantRepository extends JpaRepository<UserTenant,String> {
    List<UserTenant> findUserTenantsByRoleId(UUID id);

    UserTenant findById(UUID id);
    Long deleteByRoleId(@Param("id") UUID id);
}
