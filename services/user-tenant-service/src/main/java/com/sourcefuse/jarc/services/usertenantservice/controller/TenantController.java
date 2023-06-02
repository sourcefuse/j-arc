package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.core.enums.PermissionKey;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.TenantStatus;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantConfigRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

  private final TenantRepository tenantRepository;
  private final TenantConfigRepository tenantConfigRepository;

  @PostMapping("")
  public ResponseEntity<Object> createTenants(
    @Valid @RequestBody Tenant tenant
  ) {
    tenant.setStatus(TenantStatus.ACTIVE);
    Tenant savedTenant = tenantRepository.save(tenant);
    return new ResponseEntity<>(savedTenant, HttpStatus.CREATED);
  }

  @GetMapping("/count")
  public ResponseEntity<Object> countTenants() {
    Count count = Count.builder().totalCount(tenantRepository.count()).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @GetMapping("")
  public ResponseEntity<Object> fetchAllTenants() {
    List<Tenant> tenantList = tenantRepository.findAll();
    return new ResponseEntity<>(tenantList, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping("")
  public ResponseEntity<Count> updateAllTenants(
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
    return new ResponseEntity<>(new Count(count), HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Object> fetchTenantByID(@PathVariable("id") UUID id) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    if (
      currentUser.getTenantId().equals(id) &&
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_TENANT.getValue())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.getValue()
      );
    }
    Tenant savedTenant = tenantRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No tenant is present against given value"
        )
      );
    return new ResponseEntity<>(savedTenant, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping("{id}")
  public ResponseEntity<Object> updateTenantsById(
    @PathVariable("id") UUID id,
    @RequestBody Tenant sourceTenant
  ) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    if (
      currentUser.getTenantId().equals(id) &&
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_TENANT.getValue())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.getValue()
      );
    }
    Tenant targetTenant = tenantRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No tenant is present against given value"
        )
      );
    BeanUtils.copyProperties(
      sourceTenant,
      targetTenant,
      CommonUtils.getNullPropertyNames(sourceTenant)
    );
    tenantRepository.save(targetTenant);
    return new ResponseEntity<>("Tenant PATCH success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteTenantsById(@PathVariable("id") UUID id) {
    tenantRepository.deleteById(id);
    return new ResponseEntity<>("Tenant DELETE success", HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}/config")
  public ResponseEntity<ArrayList<TenantConfig>> getTenantConfig(
    @PathVariable("id") UUID id
  ) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    if (
      !currentUser.getTenantId().equals(id) &&
      !currentUser
        .getPermissions()
        .contains(PermissionKey.VIEW_OWN_TENANT.getValue())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.getValue()
      );
    }
    ArrayList<TenantConfig> tenantConfig =
      tenantConfigRepository.findByTenantId(id);
    return new ResponseEntity<ArrayList<TenantConfig>>(
      tenantConfig,
      HttpStatus.OK
    );
  }
}
