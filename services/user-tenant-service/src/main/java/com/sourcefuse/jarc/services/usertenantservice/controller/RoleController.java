package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

  @PostMapping
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
    Role savedRole = roleRepository.save(role);
    return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
  }

  @GetMapping("/count")
  public ResponseEntity<CountResponse> countRole() {
    CountResponse count = CountResponse
      .builder()
      .count(roleRepository.count())
      .build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Role>> getAllRoles() {
    List<Role> role = roleRepository.findAll();
    return new ResponseEntity<>(role, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping
  public ResponseEntity<CountResponse> updateAllRole(
    @RequestBody Role sourceRole
  ) {
    List<Role> updatedRoleLis = new ArrayList<>();

    List<Role> targetRoleList = roleRepository.findAll();

    long count = 0;
    if (!targetRoleList.isEmpty()) {
      for (Role targetRole : targetRoleList) {
        BeanUtils.copyProperties(
          sourceRole,
          targetRole,
          CommonUtils.getNullPropertyNames(sourceRole)
        );
        updatedRoleLis.add(targetRole);
      }
      count = roleRepository.saveAll(updatedRoleLis).size();
    }
    return new ResponseEntity<>(new CountResponse(count), HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Role> getRoleByID(@PathVariable("id") UUID id) {
    Role role = roleRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_ROLE_PRESENT
        )
      );
    return new ResponseEntity<>(role, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping("{id}")
  public ResponseEntity<String> updateRole(
    @PathVariable("id") UUID id,
    @RequestBody Role sourceRole
  ) {
    Role targetRole = roleRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_ROLE_PRESENT
        )
      );
    BeanUtils.copyProperties(
      sourceRole,
      targetRole,
      CommonUtils.getNullPropertyNames(sourceRole)
    );
    roleRepository.save(targetRole);
    return new ResponseEntity<>("Role PATCH success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @PutMapping("{id}")
  public ResponseEntity<String> updateRoleById(
    @PathVariable("id") UUID id,
    @Valid @RequestBody Role role
  ) {
    Role savedRole = roleRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_ROLE_PRESENT
        )
      );
    role.setId(savedRole.getId());
    roleRepository.save(role);
    return new ResponseEntity<>("Role PUT success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {
    roleRepository.deleteById(id);
    return new ResponseEntity<>("Roles DELETE success", HttpStatus.NO_CONTENT);
  }
}
