package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {

    public Optional<Tenant> findById(UUID id) throws ApiPayLoadException;
}
