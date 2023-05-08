package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Role;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
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
public class RoleController {

    private final RoleRepository roleRepository;

    @PostMapping("")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> count() {

        Count count = Count.builder().count(roleRepository.count()).build();
        return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllRoles() {

        List<Role> role = roleRepository.findAll();
        return new ResponseEntity<Object>(role, HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<Count> updateAll(@RequestBody Role socRole) {

        List<Role> updatedRoleLis = new ArrayList<>();

        List<Role> tarLisRole = roleRepository.findAll();

        Long count = null;
        if (tarLisRole != null) {
            for (Role tarRole : tarLisRole) {
                BeanUtils.copyProperties(socRole, tarRole, CommonUtils.getNullPropertyNames(socRole));
                updatedRoleLis.add(tarRole);
            }
            count = (long) roleRepository.saveAll(updatedRoleLis).size();

        } else {
        }
        return new ResponseEntity<Count>(new Count(count), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getRoleByID(@PathVariable("id") UUID id) {

        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No role is present against given value");
        return new ResponseEntity<Object>(role.get(), HttpStatus.OK);
    }


    @PatchMapping("{id}")
    public ResponseEntity<Object> updateRole(@PathVariable("id") UUID id, @RequestBody Role sorcRole) {
        Role targetRole;
        Optional<Role> updateRole = roleRepository.findById(id);
        if (updateRole.isPresent()) {
            targetRole = updateRole.get();
            BeanUtils.copyProperties(sorcRole, targetRole, CommonUtils.getNullPropertyNames(sorcRole));
            roleRepository.save(targetRole);

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No role is present against given value");

        return new ResponseEntity<Object>("Role PATCH success", HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> replaceRoleById(@PathVariable("id") UUID id, @RequestBody Role role) {

        role.setId(id);
        roleRepository.save(role);
        return new ResponseEntity<Object>("Role PUT success", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {

        roleRepository.deleteById(id);
        return new ResponseEntity<String>("Roles DELETE success", HttpStatus.NO_CONTENT);
    }
}




