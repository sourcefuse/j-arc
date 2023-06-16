package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.enums.TenantStatus;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

  private final TenantRepository tenantRepository;
  private final TenantService tenantService;

  @PostMapping
  public ResponseEntity<Tenant> createTenants(
    @Valid @RequestBody Tenant tenant
  ) {
    tenant.setStatus(TenantStatus.ACTIVE);
    Tenant savedTenant = tenantRepository.save(tenant);
    return new ResponseEntity<>(savedTenant, HttpStatus.CREATED);
  }

  @GetMapping("/count")
  public ResponseEntity<CountResponse> countTenants() {
    CountResponse count = CountResponse
      .builder()
      .count(tenantRepository.count())
      .build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Tenant>> fetchAllTenants() {
    List<Tenant> tenantList = tenantRepository.findAll();
    return new ResponseEntity<>(tenantList, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping
  public ResponseEntity<CountResponse> updateAllTenants(
    @RequestBody Tenant sourceTenant
  ) {
    List<Tenant> updatedListTenant = new ArrayList<>();

    List<Tenant> targetListTenant = tenantRepository.findAll();

    long count = 0;
    if (!targetListTenant.isEmpty()) {
      for (Tenant targetTenant : targetListTenant) {
        BeanUtils.copyProperties(
          sourceTenant,
          targetTenant,
          CommonUtils.getNullPropertyNames(sourceTenant)
        );
        updatedListTenant.add(targetTenant);
      }
      count = tenantRepository.saveAll(updatedListTenant).size();
    }
    return new ResponseEntity<>(new CountResponse(count), HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Tenant> fetchTenantByID(@PathVariable("id") UUID id) {
    Tenant savedTenant = tenantService.fetchTenantByID(id);
    return new ResponseEntity<>(savedTenant, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping("{id}")
  public ResponseEntity<String> updateTenantsById(
    @PathVariable("id") UUID id,
    @RequestBody Tenant sourceTenant
  ) {
    tenantService.updateTenantsById(sourceTenant, id);
    return new ResponseEntity<>("Tenant PATCH success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteTenantsById(@PathVariable("id") UUID id) {
    tenantService.deleteById(id);
    return new ResponseEntity<>("Tenant DELETE success", HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}/config")
  public ResponseEntity<List<TenantConfig>> getTenantConfig(
    @PathVariable("id") UUID id
  ) {
    List<TenantConfig> tenantConfig = tenantService.getTenantConfig(id);
    return new ResponseEntity<>(tenantConfig, HttpStatus.OK);
  }
}
