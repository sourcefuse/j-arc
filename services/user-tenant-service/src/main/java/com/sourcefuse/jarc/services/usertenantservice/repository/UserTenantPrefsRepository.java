package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserConfigKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTenantPrefsRepository
  extends JpaRepository<UserTenantPrefs, UUID> {
  UserTenantPrefs getByUserTenantIdAndConfigKey(
    UUID id,
    UserConfigKey configKey
  );
}
