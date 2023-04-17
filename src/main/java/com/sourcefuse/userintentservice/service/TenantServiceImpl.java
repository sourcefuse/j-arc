package com.sourcefuse.userintentservice.service;

import com.sourcefuse.userintentservice.DTO.Tenant;
import com.sourcefuse.userintentservice.commonutils.CommonUtils;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;
import com.sourcefuse.userintentservice.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private final TenantRepository tenantRepository;

    @Override
    public Tenant save(Tenant tenant) {

        return tenantRepository.save(tenant);
    }
    @Override
    public Tenant update(Tenant source, Tenant dest) {
        new CommonUtils<Tenant>().copyProperties(source,dest);
        log.info("updated values in models are :::::::"+dest.toString());
        return tenantRepository.save(dest);
    }

    @Override
    public Optional<Tenant> findById(UUID id) throws ApiPayLoadException {

        Optional<Tenant> tenant = tenantRepository.findById(id);
        if (tenant.isPresent()) {
            return tenant;
        } else
            throw new ApiPayLoadException("No tenant is present against given value");

    }

    @Override
    public void deleteById(String id) {
        tenantRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return tenantRepository.count();
    }

    @Override
    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public Long updateAll(List<Tenant> tenants) {

        return (long) tenantRepository.saveAll(tenants).size();
    }
}
