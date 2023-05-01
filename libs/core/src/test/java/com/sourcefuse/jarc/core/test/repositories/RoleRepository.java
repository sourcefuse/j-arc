package com.sourcefuse.jarc.core.test.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepository;
import com.sourcefuse.jarc.core.test.models.Role;

public interface RoleRepository extends SoftDeletesRepository<Role, UUID> {

}
