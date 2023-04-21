package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TenantService {


    Tenant save(Tenant tenant);


    Tenant update(Tenant source, Tenant dest);

    Optional<Tenant> findById(UUID id) throws ApiPayLoadException;

    void deleteById(String id);

    Long count();

    List<Tenant> findAll();

    Long updateAll(List<Tenant> tenants);
}
