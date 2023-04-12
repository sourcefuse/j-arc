package com.sourcefuse.jarc.services.authservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.authservice.models.Role;

public interface RoleRepository extends CrudRepository<Role, UUID> {
  Optional<Role> findByName(String name);

  Optional<Role> findByRoleType(int roleType);
}
