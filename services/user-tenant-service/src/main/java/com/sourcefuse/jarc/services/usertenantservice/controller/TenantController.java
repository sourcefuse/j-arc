package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.TenantStatus;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @PostMapping("")
  public ResponseEntity<Object> createTenants(
    @Valid @RequestBody Tenant tenant
  ) {
    tenant.setStatus(TenantStatus.ACTIVE);
    Tenant savedTenant = tenantRepository.save(tenant);
    return new ResponseEntity<>(savedTenant, HttpStatus.CREATED);
  }

  /**
   * Need to discuss about query parameter doubt
   */
  @GetMapping("/count")
  public ResponseEntity<Object> countTenants() {
    Count count = Count.builder().totalCnt(tenantRepository.count()).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  /**
   * Need to discuss about query parameter doubt
   */
  @GetMapping("")
  public ResponseEntity<Object> fetchAllTenants() {
    List<Tenant> tenantList = tenantRepository.findAll();
    return new ResponseEntity<>(tenantList, HttpStatus.OK);
  }

  /**
   * Need to discuss about query parameter doubt
   */
  @PatchMapping("")
  public ResponseEntity<Count> updateAllTenants(
    @RequestBody Tenant souctenant
  ) {
    List<Tenant> updatedListTenant = new ArrayList<>();

    List<Tenant> tarLisTenant = tenantRepository.findAll();

    long count = 0;
    if (!tarLisTenant.isEmpty()) {
      for (Tenant tarTenant : tarLisTenant) {
        BeanUtils.copyProperties(
          souctenant,
          tarTenant,
          CommonUtils.getNullPropertyNames(souctenant)
        );
        updatedListTenant.add(tarTenant);
      }
      count = tenantRepository.saveAll(updatedListTenant).size();
    }
    return new ResponseEntity<>(new Count(count), HttpStatus.OK);
  }

  /**
   * Need to discuss about query parameter doubt
   */

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
        .contains(PermissionKey.VIEW_OWN_TENANT.toString())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }
    Tenant savedTenant;
    Optional<Tenant> tenant = tenantRepository.findById(id);
    if (tenant.isPresent()) {
      savedTenant = tenant.get();
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No tenant is present against given value"
      );
    }
    return new ResponseEntity<>(savedTenant, HttpStatus.OK);
  }

  @PatchMapping("{id}")
  public ResponseEntity<Object> updateTenantsById(
    @PathVariable("id") UUID id,
    @RequestBody Tenant srcTenant
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
        .contains(PermissionKey.VIEW_OWN_TENANT.toString())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
      );
    }

    Tenant tarTenant;
    Optional<Tenant> svdTenant = tenantRepository.findById(id);
    if (svdTenant.isPresent()) {
      tarTenant = svdTenant.get();

      BeanUtils.copyProperties(
        srcTenant,
        tarTenant,
        CommonUtils.getNullPropertyNames(srcTenant)
      );
      tenantRepository.save(tarTenant);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No tenant is present against given value"
      );
    }
    return new ResponseEntity<>("Tenant PATCH success", HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("")
  public ResponseEntity<String> deleteTenantsById(@PathVariable("id") UUID id) {
    tenantRepository.deleteById(id);
    return new ResponseEntity<>("Tenant DELETE success", HttpStatus.NO_CONTENT);
  }
}
