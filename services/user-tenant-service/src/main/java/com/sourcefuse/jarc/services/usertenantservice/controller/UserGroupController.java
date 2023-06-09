package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.UserGroupService;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserGroupsSpecification;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@Slf4j
@RequestMapping("/groups")
@RequiredArgsConstructor
public class UserGroupController {

  private final GroupRepository groupRepository;

  private final UserGroupsRepository userGroupsRepo;
  private final UserGroupService userGroupService;

  @PostMapping("{id}/user-groups")
  public ResponseEntity<UserGroup> createUserGroup(
    @Valid @RequestBody UserGroup userGroup,
    @PathVariable("id") UUID id
  ) {
    UserGroup savedUserGroup = userGroupService.createUserGroup(userGroup, id);
    return new ResponseEntity<>(savedUserGroup, HttpStatus.CREATED);
  }

  @Transactional
  @PatchMapping("{id}/user-groups/{userGroupId}")
  public ResponseEntity<String> updateAll(
    @PathVariable("id") UUID id,
    @RequestBody UserGroup userGroup,
    @PathVariable("userGroupId") UUID userGroupId
  ) {
    userGroupService.updateAllUserGroup(id, userGroup, userGroupId);
    return new ResponseEntity<>(
      "Group.UserGroup PATCH success count",
      HttpStatus.OK
    );
  }

  @Transactional
  @DeleteMapping("{id}/user-groups/{userGroupId}")
  public ResponseEntity<String> deleteUsrGrp(
    @PathVariable("id") UUID id,
    @PathVariable("userGroupId") UUID userGroupId
  ) {
    userGroupService.deleteUserGroup(id, userGroupId);
    return new ResponseEntity<>("UserGroup DELETE success", HttpStatus.OK);
  }

  @GetMapping("{id}/user-groups")
  public ResponseEntity<List<UserGroup>> getAllUsTenantByRole(
    @PathVariable("id") UUID id
  ) {
    /** INFO fetch value in Group against primary key also a validation
         if group table does not have the records then  it will also not be
         available in userGroups table */
    List<UserGroup> userGroupList;
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      userGroupList =
        userGroupsRepo.findAll(UserGroupsSpecification.byGroupId(id));
      return new ResponseEntity<>(userGroupList, HttpStatus.OK);
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No group is present against given value"
      );
    }
  }

  @GetMapping("{id}/user-groups/count")
  public ResponseEntity<Count> countUserGroup(@PathVariable("id") UUID id) {
    Long groupCount = userGroupsRepo.count(
      UserGroupsSpecification.byGroupId(id)
    );
    Count count = Count.builder().totalCount(groupCount).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }
}
