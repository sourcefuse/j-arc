package com.sourcefuse.userintentservice.repository;

import com.sourcefuse.userintentservice.DTO.Count;
import com.sourcefuse.userintentservice.DTO.UserTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoleUserTenantRepository extends JpaRepository<UserTenant,String> {
    List<UserTenant> findUserTenantsByRoleId(UUID id);


    Count deleteByRoleId(@Param("id") UUID id);
}
