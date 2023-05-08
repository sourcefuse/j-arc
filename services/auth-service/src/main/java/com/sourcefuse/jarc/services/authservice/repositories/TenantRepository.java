package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.services.authservice.models.Tenant;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TenantRepository extends CrudRepository<Tenant, String> {
  Optional<Tenant> findByKey(String key);
}
