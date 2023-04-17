package com.sourcefuse.usertenantservice.controller;

import com.sourcefuse.usertenantservice.DTO.Count;
import com.sourcefuse.usertenantservice.DTO.Tenant;
import com.sourcefuse.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.usertenantservice.enums.TenantStatus;
import com.sourcefuse.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.usertenantservice.exceptions.GenericRespBuilder;
import com.sourcefuse.usertenantservice.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = {"${api.tenants.context.url}"})
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    private final GenericRespBuilder genericRespBuilder;

    private final CommonUtils<Tenant> commonUtils;

    @PostMapping("")
    public ResponseEntity<Object> createTenants(@Valid @RequestBody Tenant tenant) {

            log.info("::::::::::::: Tenant create rest Apis consumed :::::::::::::;");
            tenant.setStatus(TenantStatus.ACTIVE);
            Tenant savedTenant = tenantService.save(tenant);
            return new ResponseEntity<Object>(savedTenant, HttpStatus.CREATED);
    }

    /**
     * Need to discuss about query parameter doubt
     */
    @GetMapping(value = {"${api.upi2.count.tenants}"})
    public ResponseEntity<Object> countTenants() {
            log.info("::::::::::::: Tenant count rest Apis consumed :::::::::::::;");
            Count count = new Count();
            count.setCount(tenantService.count());
            return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

    /**
     * Need to discuss about query parameter doubt
     */
    @GetMapping("")
    public ResponseEntity<Object> fetchAllTenants() {
            log.info("::::::::::::: Tenant  Rest Apis consumed to get total Tenants present:::::::::::::;");
            List<Tenant> TenantList = tenantService.findAll();
            return new ResponseEntity<Object>(TenantList, HttpStatus.OK);
    }

    /**
     * Need to discuss about query parameter doubt
     */
    @PatchMapping("")
    public ResponseEntity<Count> updateAllTenants(@RequestBody Tenant Souctenant) {

        log.info("::::::::::::: Tenant  Rest Apis consumed to update all tenants present :::::::::::::;");
        List<Tenant> desListTenat = new ArrayList<Tenant>();

        List<Tenant> DestlisTenant = tenantService.findAll();

        Long count = null;
        if (DestlisTenant != null) {
            for (Tenant DesTenant : DestlisTenant) {
                commonUtils.copyProperties(Souctenant, DesTenant);
                desListTenat.add(DesTenant);
            }
            count = tenantService.updateAll(desListTenat);

        } else {
            log.error(" :::::::::::::: No tenants exits ::::::::::::::");
        }
        return new ResponseEntity<Count>(new Count(count), HttpStatus.OK);
    }

    /**
     * Need to discuss about query parameter doubt
     */

    @GetMapping("{id}")
    public ResponseEntity<Object> fetchTenantByID(@PathVariable("id") UUID id) throws ApiPayLoadException {

            log.info("::::::::::::: Fetch Tenant  rest Apis consumed based on id:::::::::::::;");
            Tenant savedTenant = tenantService.findById(id).get();
            return new ResponseEntity<Object>(savedTenant, HttpStatus.OK);
    }


    @PatchMapping("{id}")
    public ResponseEntity<Object> updateTenantsById(@PathVariable("id") UUID id, @RequestBody Tenant tenant) throws ApiPayLoadException {
            log.info("Tenant update rest Apis consumed based on id ");
            Tenant updateTenant = tenantService.findById(id).get();
            log.info(updateTenant.toString());

            tenantService.update(tenant, updateTenant);
            return new ResponseEntity<Object>("Tenant PATCH success", HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteTenantsById(@PathVariable("id") String id) {
        log.info("::::::::::::: Tenant Delete rest Apis consumed :::::::::::::;");
        tenantService.deleteById(id);
        return new ResponseEntity<String>("Tenant DELETE success", HttpStatus.NO_CONTENT);
    }

    /**
     * Need to discuss about query parameter doubt
     */
   /* @GetMapping("/{id}/config")
    public ResponseEntity<ArrayList<TenantConfig>> getTenantConfig(@PathVariable("id") String id) {
      //  ArrayList<TenantConfig> tenantConfig=tenantConfigRepository.findById(id);

        return new ResponseEntity<ArrayList<TenantConfig>>(tenantConfig, HttpStatus.Ok);
    }*/

}
