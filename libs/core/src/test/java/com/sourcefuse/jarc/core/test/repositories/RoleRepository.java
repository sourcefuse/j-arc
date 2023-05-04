package com.sourcefuse.jarc.core.test.repositories;

import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepository;
import com.sourcefuse.jarc.core.test.models.Role;
import java.util.UUID;

public interface RoleRepository extends SoftDeletesRepository<Role, UUID> {}
