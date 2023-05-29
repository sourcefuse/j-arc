package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserConfigKey;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTenantPrefsRepository
  extends SoftDeletesRepository<UserTenantPrefs, UUID> {
  UserTenantPrefs getByUserTenantIdAndConfigKey(
    UUID id,
    UserConfigKey configKey
  );
}
