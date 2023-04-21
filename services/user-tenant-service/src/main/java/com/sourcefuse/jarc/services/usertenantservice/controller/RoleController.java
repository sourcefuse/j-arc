package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Role;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.GenericRespBuilder;
import com.sourcefuse.jarc.services.usertenantservice.service.RoleService;
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

        log.info("::::::::::::: Roles count rest Apis consumed :::::::::::::;");

        Count count = Count.builder().count(roleService.count()).build();
        return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllRoles() {

        log.info("::::::::::::: Roles  Rest Apis consumed to get total Roles present:::::::::::::;");
        List<Role> role = roleService.findAll();
        return new ResponseEntity<Object>(role, HttpStatus.OK);
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
    public ResponseEntity<Object> getRoleByID(@PathVariable("id") UUID id) throws ApiPayLoadException, Exception {

        log.info("::::::::::::: Fetch Role based on id::::::::::::"+id);
        Role role = roleService.findById(id).get();
        return new ResponseEntity<Object>(role, HttpStatus.OK);
    }


    @PatchMapping("{id}")
    public ResponseEntity<Object> updateRole(@PathVariable("id") UUID id, @RequestBody Role role) throws ApiPayLoadException {

        log.info("Role update rest Apis consumed based on id ");
        Role updaterole = roleService.findById(id).get();
        //  updateTenant.setAddress(tenant.getAddress());
        log.info(updaterole.toString());

        roleService.update(role, updaterole);
        return new ResponseEntity<Object>("Role PATCH success", HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> replaceRoleById(@PathVariable("id") UUID id, @RequestBody Role role) {

        log.info(" ::::: Role  rest Apis consumed  to update all the records against ID ::::");
        role.setId(id);
        roleService.save(role);
        return new ResponseEntity<Object>("Role PUT success", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {
        log.info("::::::::::::: ROle Delete rest Apis consumed :::::::::::::;");
        roleService.deleteById(id);

        return new ResponseEntity<String>("Roles DELETE success", HttpStatus.NO_CONTENT);
    }
}




