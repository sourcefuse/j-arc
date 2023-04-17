package com.sourcefuse.usertenantservice.repository;

import com.sourcefuse.usertenantservice.DTO.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role,UUID > {

    //public Optional<Role> findById(UUID id) throws ApiPayLoadException;
}
