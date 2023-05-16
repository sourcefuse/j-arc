package com.sourcefuse.jarc.services.authservice.repositories;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.services.authservice.models.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, UUID> {
  Optional<Role> findByName(String name);

  Optional<Role> findByRoleType(RoleKey roleType);
}
