package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.enums.TenantStatus;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/tenants")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TenantController {

  private final TenantRepository tenantRepository;
  private final TenantService tenantService;

  @PostMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.CREATE_TENANT +
    "')"
  )
  public ResponseEntity<Tenant> createTenants(
    @Valid @RequestBody Tenant tenant
  ) {
    tenant.setStatus(TenantStatus.ACTIVE);
    Tenant savedTenant = tenantRepository.save(tenant);
    return new ResponseEntity<>(savedTenant, HttpStatus.CREATED);
  }

  @GetMapping("/count")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_TENANT +
    "')"
  )
  public ResponseEntity<CountResponse> countTenants() {
    return new ResponseEntity<>(
      CountResponse.builder().count(tenantRepository.count()).build(),
      HttpStatus.OK
    );
  }

  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_TENANT +
    "')"
  )
  public ResponseEntity<List<Tenant>> fetchAllTenants() {
    return new ResponseEntity<>(tenantRepository.findAll(), HttpStatus.OK);
  }

  @Transactional
  @PatchMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.UPDATE_TENANT +
    "')"
  )
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
    return new ResponseEntity<>(
      CountResponse.builder().count(count).build(),
      HttpStatus.OK
    );
  }

  @GetMapping("{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.VIEW_TENANT +
    "','" +
    PermissionKeyConstants.VIEW_OWN_TENANT +
    "')"
  )
  public ResponseEntity<Tenant> fetchTenantByID(@PathVariable("id") UUID id) {
    return new ResponseEntity<>(
      tenantService.fetchTenantByID(id),
      HttpStatus.OK
    );
  }

  @Transactional
  @PatchMapping("{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.UPDATE_TENANT +
    "','" +
    PermissionKeyConstants.UPDATE_OWN_TENANT +
    "')"
  )
  public ResponseEntity<String> updateTenantsById(
    @PathVariable("id") UUID id,
    @RequestBody Tenant sourceTenant
  ) {
    tenantService.updateTenantsById(sourceTenant, id);
    return new ResponseEntity<>("Tenant PATCH success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @DeleteMapping("{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.DELETE_TENANT +
    "')"
  )
  public ResponseEntity<String> deleteTenantsById(@PathVariable("id") UUID id) {
    tenantService.deleteById(id);
    return new ResponseEntity<>("Tenant DELETE success", HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}/config")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.VIEW_TENANT +
    "','" +
    PermissionKeyConstants.VIEW_OWN_TENANT +
    "')"
  )
  public ResponseEntity<List<TenantConfig>> getTenantConfig(
    @PathVariable("id") UUID id
  ) {
    return new ResponseEntity<>(
      tenantService.getTenantConfig(id),
      HttpStatus.OK
    );
  }
}
