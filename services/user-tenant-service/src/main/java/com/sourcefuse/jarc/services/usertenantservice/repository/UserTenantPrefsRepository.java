package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTenantPrefsRepository
  extends JpaRepository<UserTenantPrefs, UUID> {
  UserTenantPrefs findByUserTenantIdAndConfigKey(UUID id, String configKey);
}
