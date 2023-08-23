package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.core.filters.services.QueryService;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user-groups")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserGroupsController {

  private final UserGroupsRepository userGroupsRepository;
  private final QueryService queryService;

  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_USER_GROUP_LIST +
    "')"
  )
  public ResponseEntity<List<UserGroup>> fetchAllUserGroups(@RequestParam(required = false,name = "filter") Filter filter) {
    Specification<UserGroup> userGroupSpecifications = queryService.getSpecifications(filter);
    return new ResponseEntity<>(userGroupsRepository.findAll(userGroupSpecifications), HttpStatus.OK);
  }

  @GetMapping("/count")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    PermissionKeyConstants.VIEW_USER_GROUP_LIST +
    "')"
  )
  public ResponseEntity<CountResponse> countTenants(@RequestParam(required = false,name = "filter") Filter filter) {

    Specification<UserGroup> userGroupSpecifications = queryService.getSpecifications(filter);
    List<UserGroup> userGroupsList = userGroupsRepository.findAll(userGroupSpecifications);

    return new ResponseEntity<>(
      CountResponse.builder().count((long) userGroupsList.size()).build(),
      HttpStatus.OK
    );
  }
}
