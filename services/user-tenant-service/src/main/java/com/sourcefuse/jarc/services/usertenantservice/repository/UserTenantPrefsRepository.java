package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenantPrefs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTenantPrefsRepository extends JpaRepository<UserTenantPrefs, UUID> {
    @Query(value = "select * from main.user_tenant_prefs where user_tenant_id=:id and config_key=:configKey" ,nativeQuery = true)
    UserTenantPrefs findByUserTenantIdConfKey(UUID id,String configKey );

}
