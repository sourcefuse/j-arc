package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserTenantSpecification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleUserTenantController {

  private final RoleRepository roleRepository;

  private final RoleUserTenantRepository roleUserTenantRepository;

  @PostMapping("{id}/user-tenants")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.NOT_ALLOWED +
    "')"
  )
  public ResponseEntity<UserTenant> createRoleUserTenant(
    @Validated @RequestBody UserTenant userTenant,
    @PathVariable("id") UUID id
  ) {
    UserTenant savedUserRole;
    Role role = roleRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No role is present against given value"
        )
      );
    userTenant.getRole().setId(role.getId());
    savedUserRole = roleUserTenantRepository.save(userTenant);
    return new ResponseEntity<>(savedUserRole, HttpStatus.CREATED);
  }

  @GetMapping("{id}/user-tenants")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_ROLES +
    "')"
  )
  public ResponseEntity<List<UserTenant>> getAllUserTenantByRole(
    @PathVariable("id") UUID id
  ) {
    List<UserTenant> tenantList = roleUserTenantRepository.findAll(
      UserTenantSpecification.byRoleId(id)
    );
    return new ResponseEntity<>(tenantList, HttpStatus.OK);
  }

  @GetMapping("{id}/user-tenants/count")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_ROLES +
    "')"
  )
  public ResponseEntity<CountResponse> countUserTenantByRole(
    @PathVariable("id") UUID id
  ) {
    List<UserTenant> tenantList = roleUserTenantRepository.findAll(
      UserTenantSpecification.byRoleId(id)
    );
    return new ResponseEntity<>(
      CountResponse.builder().count((long) tenantList.size()).build(),
      HttpStatus.OK
    );
  }

  @Transactional
  @PatchMapping("{id}/user-tenants")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.NOT_ALLOWED +
    "')"
  )
  public ResponseEntity<CountResponse> updateAll(
    @PathVariable("id") UUID id,
    @RequestBody UserTenant sourceUserTenant
  ) {
    List<UserTenant> targetUserTenantArrayList = new ArrayList<>();
    List<UserTenant> userTenantArrayList = roleUserTenantRepository.findAll(
      UserTenantSpecification.byRoleId(id)
    );
    long count = 0;
    if (!userTenantArrayList.isEmpty()) {
      for (UserTenant targetUserTenant : userTenantArrayList) {
        BeanUtils.copyProperties(
          sourceUserTenant,
          targetUserTenant,
          CommonUtils.getNullPropertyNames(sourceUserTenant)
        );
        targetUserTenantArrayList.add(targetUserTenant);
      }
      count =
        roleUserTenantRepository.saveAll(targetUserTenantArrayList).size();
    }
    return new ResponseEntity<>(
      CountResponse.builder().count(count).build(),
      HttpStatus.OK
    );
  }

  @Transactional
  @DeleteMapping("{id}/user-tenants")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.NOT_ALLOWED +
    "')"
  )
  public ResponseEntity<CountResponse> deleteRolesById(
    @PathVariable("id") UUID id
  ) {
    long count = roleUserTenantRepository.delete(
      UserTenantSpecification.byRoleId(id)
    );
    return new ResponseEntity<>(
      CountResponse.builder().count(count).build(),
      HttpStatus.OK
    );
  }
}
