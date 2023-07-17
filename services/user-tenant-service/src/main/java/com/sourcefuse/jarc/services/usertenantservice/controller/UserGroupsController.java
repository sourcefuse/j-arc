package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user-groups")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserGroupsController {

  private final UserGroupsRepository userGroupsRepository;

  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_USER_GROUP_LIST +
    "')"
  )
  public ResponseEntity<List<UserGroup>> fetchAllUserGroups() {
    return new ResponseEntity<>(userGroupsRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping("/count")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_USER_GROUP_LIST +
    "')"
  )
  public ResponseEntity<CountResponse> countTenants() {
    List<UserGroup> userGroupsList = userGroupsRepository.findAll();
    return new ResponseEntity<>(
      CountResponse.builder().count((long) userGroupsList.size()).build(),
      HttpStatus.OK
    );
  }
}
