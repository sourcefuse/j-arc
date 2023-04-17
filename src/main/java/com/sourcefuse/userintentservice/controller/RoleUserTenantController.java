package com.sourcefuse.userintentservice.controller;

import com.sourcefuse.userintentservice.DTO.Count;
import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.DTO.UserTenant;
import com.sourcefuse.userintentservice.commonutils.CommonUtils;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;
import com.sourcefuse.userintentservice.exceptions.GenericRespBuilder;
import com.sourcefuse.userintentservice.service.RoleService;
import com.sourcefuse.userintentservice.service.RoleUserTenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = {"${api.roles.context.url}"})
@RequiredArgsConstructor
public class RoleUserTenantController {

    private final RoleUserTenantService roleUserTenantService;

    private final RoleService roleService;

    private final GenericRespBuilder genericRespBuilder;

    private final CommonUtils<UserTenant> commonUtils;

    @PostMapping("{id}" + "${api.roles.user-tenant.context.url}")
    public ResponseEntity<Object> createRole(@Valid @RequestBody UserTenant userTenant, @PathVariable("id") UUID id) {
        log.info(" ::::::::::::  Roles User Tenant Apis consumed for creating roles::::::::::::;;;");

        try {
            Role role = roleService.findById(id).get();
            userTenant.setRoleId(role.getId());
            // log.info(role.toString());
            role.getUserTenants().add(userTenant);
            // log.info(userTenant.toString());
            UserTenant savedUserRole = roleUserTenantService.save(userTenant);
            return new ResponseEntity<>(savedUserRole, HttpStatus.CREATED);

        } catch (ApiPayLoadException exp) {
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", exp.getErrMsg()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("{id}" + "${api.roles.user-tenant.context.url}")
    public ResponseEntity<Object> getAllUsTenantByRole(@PathVariable("id") UUID id) {
        try {
            log.info("::::::::::::: Fetch Role has many UserTenant :::::::::::::;");
            List<UserTenant> LUsTenant = roleUserTenantService.findUserTenantsByRoleId(id);
            return new ResponseEntity<Object>(LUsTenant, HttpStatus.OK);
        } catch (Exception ex) {
            ex.getStackTrace();
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("{id}" + "${api.roles.user-tenant.count.context.url}")
    public ResponseEntity<Object> countUserTenantByRole(@PathVariable("id") UUID id) {
        try {
            log.info("::::::::::::: User tenant count for specified role id :::::::::::::;");
            List<UserTenant> LUsTenant = roleUserTenantService.findUserTenantsByRoleId(id);

            return new ResponseEntity<Object>(new Count((long) LUsTenant.size()), HttpStatus.OK);
        } catch (Exception ex) {
            ex.getStackTrace();
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PatchMapping("{id}" + "${api.roles.user-tenant.context.url}")
    public ResponseEntity<Count> updateAll(@PathVariable("id") UUID id, @RequestBody UserTenant socUserTenant) {

        log.info("::::::::::::: Role  Rest Apis consumed to update all tenants present :::::::::::::;");
        List<UserTenant> tarUserTenantArrayList = new ArrayList<>();

        List<UserTenant> UserTenantArrayList = roleUserTenantService.findUserTenantsByRoleId(id);

        Long count = null;
        if (UserTenantArrayList != null) {
            for (UserTenant tarUserTenant : UserTenantArrayList) {
                commonUtils.copyProperties(socUserTenant, tarUserTenant);
                tarUserTenantArrayList.add(tarUserTenant);
            }
            count = roleUserTenantService.updateAll(tarUserTenantArrayList);

        } else {
            log.error(" :::::::::::::: No Role exits ::::::::::::::");
        }


        return new ResponseEntity<Count>(new Count(count), HttpStatus.OK);
    }

    @DeleteMapping("{id}" + "${api.roles.user-tenant.context.url}")
    public ResponseEntity<Object> deleteRolesById(@PathVariable("id") UUID id) {
        log.info("::::::::::::: Role User Tenant Delete rest Apis consumed :::::::::::::;");
        Count count=roleUserTenantService.DeleteUserTenantsByRoleId(id);
        return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

}
