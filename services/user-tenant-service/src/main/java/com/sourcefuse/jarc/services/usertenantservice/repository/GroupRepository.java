package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, UUID> {}
