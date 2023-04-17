package com.sourcefuse.userintentservice.controller;

import com.sourcefuse.userintentservice.DTO.Count;
import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.DTO.Tenant;
import com.sourcefuse.userintentservice.commonutils.CommonUtils;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;
import com.sourcefuse.userintentservice.exceptions.GenericRespBuilder;
import com.sourcefuse.userintentservice.service.RoleService;
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
public class RoleController {

    private final RoleService roleService;

    private final GenericRespBuilder genericRespBuilder;

    private final CommonUtils<Role> commonUtils;


    @PostMapping("")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        log.info(" ::::::::::::  Roles Apis consumed for creating roles::::::::::::;;;");
        Role savedRole = roleService.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @GetMapping(value = {"${api.upi2.count.roles}"})
    public ResponseEntity<Object> count() {
        try {
            log.info("::::::::::::: Roles count rest Apis consumed :::::::::::::;");
            Count count = new Count();
            count.setCount(roleService.count());
            return new ResponseEntity<Object>(count, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllRoles() {
        try {
            log.info("::::::::::::: Roles  Rest Apis consumed to get total Roles present:::::::::::::;");
            List<Role> role = roleService.findAll();
            return new ResponseEntity<Object>(role, HttpStatus.OK);
        } catch (Exception ex) {
            ex.getStackTrace();
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PatchMapping("")
    public ResponseEntity<Count> updateAll(@RequestBody Role socRole) {

        log.info("::::::::::::: Role  Rest Apis consumed to update all tenants present :::::::::::::;");
        List<Role> desRole = new ArrayList<>();

        List<Role> DestlisRole = roleService.findAll();

        Long count = null;
        if (DestlisRole != null) {
            for (Role DesRole : DestlisRole) {
                commonUtils.copyProperties(socRole, DesRole);
                desRole.add(DesRole);
            }
            count = roleService.updateAll(desRole);

        } else {
            log.error(" :::::::::::::: No Role exits ::::::::::::::");
        }


        return new ResponseEntity<Count>(new Count(count), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getRoleByID(@PathVariable("id") UUID id) {
        try {
            log.info("::::::::::::: Role Tenant  rest Apis consumed based on id:::::::::::::;");
            Role role = roleService.findById(id).get();
            return new ResponseEntity<Object>(role, HttpStatus.OK);
        } catch (ApiPayLoadException exp) {
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", exp.getErrMsg()),
                    HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PatchMapping("{id}")
    public ResponseEntity<Object> updateRoleById(@PathVariable("id") UUID id, @RequestBody Role role) {
        try {
            log.info("Role update rest Apis consumed based on id ");
            Role updaterole = roleService.findById(id).get();
            //  updateTenant.setAddress(tenant.getAddress());
            log.info(updaterole.toString());

            roleService.update(role, updaterole);
            return new ResponseEntity<Object>("Role PATCH success", HttpStatus.NO_CONTENT);
        } catch (ApiPayLoadException exp) {
            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", exp.getErrMsg()),
                    HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> replaceRoleById(@PathVariable("id") UUID id, @RequestBody Role role) {
        try {
            log.info(" ::::: Role  rest Apis consumed  to update all the records against ID ::::");
            role.setId(id);
            roleService.save(role);
            return new ResponseEntity<Object>("Role PUT success", HttpStatus.NO_CONTENT);

        } catch (Exception ex) {

            return new ResponseEntity<Object>(genericRespBuilder.buildGenerResp("", ex.getMessage()),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {
        log.info("::::::::::::: ROle Delete rest Apis consumed :::::::::::::;");
        roleService.deleteById(id);

        return new ResponseEntity<String>("Roles DELETE success", HttpStatus.NO_CONTENT);
    }
}




