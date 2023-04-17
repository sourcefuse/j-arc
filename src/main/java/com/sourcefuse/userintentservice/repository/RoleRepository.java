package com.sourcefuse.userintentservice.repository;

import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.DTO.Tenant;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role,UUID > {

    //public Optional<Role> findById(UUID id) throws ApiPayLoadException;
}
