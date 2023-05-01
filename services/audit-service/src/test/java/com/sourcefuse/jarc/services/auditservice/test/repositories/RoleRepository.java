package com.sourcefuse.jarc.services.auditservice.test.repositories;

import java.util.UUID;

import com.sourcefuse.jarc.services.auditservice.audit.softdelete.SoftDeletesRepository;
import com.sourcefuse.jarc.services.auditservice.test.models.Role;


public interface RoleRepository extends SoftDeletesRepository<Role, UUID> {

}
