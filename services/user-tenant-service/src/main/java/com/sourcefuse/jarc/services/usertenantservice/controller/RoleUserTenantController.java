package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

  @PostMapping("{id}/user-tenants")
  public ResponseEntity<Object> createRole(
    @Valid @RequestBody UserTenant userTenant,
    @PathVariable("id") UUID id
  ) {
    UserTenant savedUserRole;

    Optional<Role> role = roleRepository.findById(id);
    if (role.isPresent()) {
      userTenant.getRole().setId(role.get().getId());

      role.get().getUserTenants().add(userTenant);
      savedUserRole = roleUserTRepository.save(userTenant);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No role is present against given value"
      );
    }
    return new ResponseEntity<>(savedUserRole, HttpStatus.CREATED);
  }

  @GetMapping("{id}/user-tenants")
  public ResponseEntity<Object> getAllUserTenantByRole(
    @PathVariable("id") UUID id
  ) {
    List<UserTenant> tenantList = roleUserTRepository.findUserTenantsByRoleId(
      id
    );
    return new ResponseEntity<>(tenantList, HttpStatus.OK);
  }

  @GetMapping("{id}/user-tenants/count")
  public ResponseEntity<Object> countUserTenantByRole(
    @PathVariable("id") UUID id
  ) {
    List<UserTenant> tenantList = roleUserTRepository.findUserTenantsByRoleId(
      id
    );

    return new ResponseEntity<>(
      new Count((long) tenantList.size()),
      HttpStatus.OK
    );
  }

  @PatchMapping("{id}/user-tenants")
  public ResponseEntity<Count> updateAll(
    @PathVariable("id") UUID id,
    @RequestBody UserTenant socUserTenant
  ) {
    List<UserTenant> tarUserTenantArrayList = new ArrayList<>();

    List<UserTenant> userTenantArrayList =
      roleUserTRepository.findUserTenantsByRoleId(id);
    long count = 0;
    if (!userTenantArrayList.isEmpty()) {
      for (UserTenant tarUserTenant : userTenantArrayList) {
        BeanUtils.copyProperties(
          socUserTenant,
          tarUserTenant,
          CommonUtils.getNullPropertyNames(socUserTenant)
        );
        tarUserTenantArrayList.add(tarUserTenant);
      }
      count = roleUserTRepository.saveAll(tarUserTenantArrayList).size();
    }
    return new ResponseEntity<>(new Count(count), HttpStatus.OK);
  }

  @Transactional
  @DeleteMapping("{id}/user-tenants")
  public ResponseEntity<Object> deleteRolesById(@PathVariable("id") UUID id) {
    long count = roleUserTRepository.deleteByRoleId(id);
    Count cnt = Count.builder().totalCnt(count).build();
    return new ResponseEntity<>(cnt, HttpStatus.OK);
  }
}
