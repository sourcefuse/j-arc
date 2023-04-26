package com.sourcefuse.jarc.services.auditservice.test.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sourcefuse.jarc.services.auditservice.test.models.Role;


public interface RoleRepository extends JpaRepository<Role, UUID> {

}
