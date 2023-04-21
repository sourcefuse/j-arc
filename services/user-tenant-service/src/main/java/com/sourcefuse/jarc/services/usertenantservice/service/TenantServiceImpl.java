package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
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
        log.info("fetch  Tenants  against Id "+id);
        Optional<Tenant> tenant = tenantRepository.findById(id);
        if (tenant.isPresent()) {
            return tenant;
        } else
            throw new ApiPayLoadException("No tenant is present against given value");

    }

    @Override
    public void deleteById(String id) {
        log.info("delete  Tenants  against Id  "+id);
        tenantRepository.deleteById(id);
    }

    @Override
    public Long count() {
        log.info(" :::: Count total no of  Tenants  Present ::::");
        return tenantRepository.count();
    }

    @Override
    public List<Tenant> findAll() {
        log.info(" :::: Fetch  total no of  Tenants  Present ::::");
        return tenantRepository.findAll();
    }

    @Override
    public Long updateAll(List<Tenant> tenants) {
        log.info(" :::: Update  all the  Tenants  Present ::::");
        return (long) tenantRepository.saveAll(tenants).size();
    }
}
