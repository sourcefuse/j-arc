package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.authservice.models.Tenant;

public interface TenantRepository extends CrudRepository<Tenant, String> {
  Optional<Tenant> findByKey(String key);
}
