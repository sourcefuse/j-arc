package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Role;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleUserTenantController {

    private final RoleRepository roleRepository;

    private final RoleUserTenantRepository roleUserTRepository;


    @PostMapping("{id}" + "/user-tenants")
    public ResponseEntity<Object> createRole(@Valid @RequestBody UserTenant userTenant, @PathVariable("id") UUID id) {

        UserTenant savedUserRole;

        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            userTenant.setRoleId(role.get().getId());

            role.get().getUserTenants().add(userTenant);

            savedUserRole = roleUserTRepository.save(userTenant);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No role is present against given value");

        return new ResponseEntity<>(savedUserRole, HttpStatus.CREATED);

    }

    @GetMapping("{id}" + "/user-tenants")
    public ResponseEntity<Object> getAllUsTenantByRole(@PathVariable("id") UUID id) {

        List<UserTenant> LUsTenant = roleUserTRepository.findUserTenantsByRoleId(id);
        return new ResponseEntity<Object>(LUsTenant, HttpStatus.OK);
    }

    @GetMapping("{id}" + "/user-tenants/count")
    public ResponseEntity<Object> countUserTenantByRole(@PathVariable("id") UUID id) {

        List<UserTenant> LUsTenant = roleUserTRepository.findUserTenantsByRoleId(id);

        return new ResponseEntity<Object>(new Count((long) LUsTenant.size()), HttpStatus.OK);
    }

    @PatchMapping("{id}" + "/user-tenants")
    public ResponseEntity<Count> updateAll(@PathVariable("id") UUID id, @RequestBody UserTenant socUserTenant) {

        List<UserTenant> tarUserTenantArrayList = new ArrayList<>();

        List<UserTenant> UserTenantArrayList = roleUserTRepository.findUserTenantsByRoleId(id);

        long count = 0;
        if (UserTenantArrayList != null) {
            for (UserTenant tarUserTenant : UserTenantArrayList) {
                BeanUtils.copyProperties(socUserTenant, tarUserTenant, CommonUtils.getNullPropertyNames(socUserTenant));
                tarUserTenantArrayList.add(tarUserTenant);
            }
            count = roleUserTRepository.saveAll(tarUserTenantArrayList).size();
        } else {

        }
        return new ResponseEntity<Count>(new Count(count), HttpStatus.OK);
    }

    @DeleteMapping("{id}" + "/user-tenants")
    public ResponseEntity<Object> deleteRolesById(@PathVariable("id") UUID id) {
        long count = roleUserTRepository.deleteByRoleId(id);
        Count cnt = Count.builder().count(count).build();
        return new ResponseEntity<Object>(cnt, HttpStatus.OK);
    }

}
