package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Tenant;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.TenantStatus;
import com.sourcefuse.jarc.services.usertenantservice.repository.TenantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantRepository tenantRepository;

    @PostMapping("")
    public ResponseEntity<Object> createTenants(@Valid @RequestBody Tenant tenant) {

        tenant.setStatus(TenantStatus.ACTIVE);
        Tenant savedTenant = tenantRepository.save(tenant);
        return new ResponseEntity<Object>(savedTenant, HttpStatus.CREATED);
    }

    /**
     * Need to discuss about query parameter doubt
     */
    @GetMapping("/count")
    public ResponseEntity<Object> countTenants() {
        Count count = Count.builder().count(tenantRepository.count()).build();
        return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

    /**
     * Need to discuss about query parameter doubt
     */
    @GetMapping("")
    public ResponseEntity<Object> fetchAllTenants() {
        List<Tenant> TenantList = tenantRepository.findAll();
        return new ResponseEntity<Object>(TenantList, HttpStatus.OK);
    }

    /**
     * Need to discuss about query parameter doubt
     */
    @PatchMapping("")
    public ResponseEntity<Count> updateAllTenants(@RequestBody Tenant Souctenant) {
        List<Tenant> updatedListTenant = new ArrayList<Tenant>();

        List<Tenant> tarLisTenant = tenantRepository.findAll();

        long count = 0;
        if (tarLisTenant != null) {
            for (Tenant tarTenant : tarLisTenant) {
                BeanUtils.copyProperties(Souctenant, tarTenant, CommonUtils.getNullPropertyNames(Souctenant));
                updatedListTenant.add(tarTenant);
            }
            count = tenantRepository.saveAll(updatedListTenant).size();

        } else {
        }
        return new ResponseEntity<Count>(new Count(count), HttpStatus.OK);
    }

    /**
     * Need to discuss about query parameter doubt
     */

    @GetMapping("{id}")
    public ResponseEntity<Object> fetchTenantByID(@PathVariable("id") UUID id) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (currentUser.getTenantId() != id &&
                !currentUser.getPermissions().contains(PermissionKey.ViewOwnTenant.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());

        }
        Tenant savedTenant;
        Optional<Tenant> tenant = tenantRepository.findById(id);
        if (tenant.isPresent()) {
            savedTenant = tenant.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tenant is present against given value");

        return new ResponseEntity<Object>(savedTenant, HttpStatus.OK);
    }


    @PatchMapping("{id}")
    public ResponseEntity<Object> updateTenantsById(@PathVariable("id") UUID id, @RequestBody Tenant srcTenant) {

        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (currentUser.getTenantId() != id &&
                !currentUser.getPermissions().contains(PermissionKey.ViewOwnTenant.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());

        }

        Tenant tarTenant;
        Optional<Tenant> svdTenant = tenantRepository.findById(id);
        if (svdTenant.isPresent()) {
            tarTenant = svdTenant.get();

            BeanUtils.copyProperties(srcTenant, tarTenant, CommonUtils.getNullPropertyNames(srcTenant));
            tenantRepository.save(tarTenant);

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tenant is present against given value");

        return new ResponseEntity<Object>("Tenant PATCH success", HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteTenantsById(@PathVariable("id") UUID id) {
        tenantRepository.deleteById(id);
        return new ResponseEntity<String>("Tenant DELETE success", HttpStatus.NO_CONTENT);
    }

    /**
     * Need to discuss about query parameter doubt
     */
   /* @GetMapping("/{id}/config")
    public ResponseEntity<ArrayList<TenantConfig>> getTenantConfig(@PathVariable("id") String id) {
      //  ArrayList<TenantConfig> tenantConfig=tenantConfigRepository.findById(id);

         IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (currentUser.getTenantId() != id &&
                !currentUser.getPermissions().contains(PermissionKey.ViewOwnTenant.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NotAllowedAccess.toString());

        }
        return new ResponseEntity<ArrayList<TenantConfig>>(tenantConfig, HttpStatus.Ok);
    }*/

}
