package com.sourcefuse.jarc.services.auditservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sourcefuse.jarc.services.auditservice.models.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

}
