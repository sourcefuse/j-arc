package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenantPrefs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTenantPrefsRepository extends JpaRepository<UserTenantPrefs, UUID> {

    UserTenantPrefs findByUserTenantIdAndConfigKey(UUID id, String configKey);

}
