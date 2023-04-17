package com.sourcefuse.userintentservice.service;

import com.sourcefuse.userintentservice.DTO.Tenant;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;
import org.springframework.stereotype.Service;

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
