package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends SoftDeletesRepository<Group, UUID> {}
