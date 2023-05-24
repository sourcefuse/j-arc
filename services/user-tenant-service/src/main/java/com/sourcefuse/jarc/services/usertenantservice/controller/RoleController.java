package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.commonutils.CommonUtils;
import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    Count count = Count.builder().totalCnt(roleRepository.count()).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @GetMapping("")
  public ResponseEntity<Object> getAllRoles() {
    List<Role> role = roleRepository.findAll();
    return new ResponseEntity<>(role, HttpStatus.OK);
  }

  @PatchMapping("")
  public ResponseEntity<Count> updateAll(@RequestBody Role socRole) {
    List<Role> updatedRoleLis = new ArrayList<>();

    List<Role> tarLisRole = roleRepository.findAll();

    long count = 0;
    if (!tarLisRole.isEmpty()) {
      for (Role tarRole : tarLisRole) {
        BeanUtils.copyProperties(
          socRole,
          tarRole,
          CommonUtils.getNullPropertyNames(socRole)
        );
        updatedRoleLis.add(tarRole);
      }
      count = roleRepository.saveAll(updatedRoleLis).size();
    }
    return new ResponseEntity<>(new Count(count), HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Object> getRoleByID(@PathVariable("id") UUID id) {
    Optional<Role> role = roleRepository.findById(id);
    if (role.isPresent()) {
      return new ResponseEntity<>(role.get(), HttpStatus.OK);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No role is present against given value"
      );
    }
  }

  @PatchMapping("{id}")
  public ResponseEntity<Object> updateRole(
    @PathVariable("id") UUID id,
    @RequestBody Role sorcRole
  ) {
    Role targetRole;
    Optional<Role> updateRole = roleRepository.findById(id);
    if (updateRole.isPresent()) {
      targetRole = updateRole.get();
      BeanUtils.copyProperties(
        sorcRole,
        targetRole,
        CommonUtils.getNullPropertyNames(sorcRole)
      );
      roleRepository.save(targetRole);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No role is present against given value"
      );
    }
    return new ResponseEntity<>("Role PATCH success", HttpStatus.NO_CONTENT);
  }

  @PutMapping("{id}")
  public ResponseEntity<Object> replaceRoleById(
    @PathVariable("id") UUID id,
    @RequestBody Role role
  ) {
    role.setId(id);
    roleRepository.save(role);
    return new ResponseEntity<>("Role PUT success", HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {
    roleRepository.deleteById(id);
    return new ResponseEntity<>("Roles DELETE success", HttpStatus.NO_CONTENT);
  }
}
