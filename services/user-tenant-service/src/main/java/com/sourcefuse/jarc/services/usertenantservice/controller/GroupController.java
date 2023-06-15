package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.commons.CurrentUserUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.GroupSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserGroupsSpecification;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class GroupController {

  private final GroupRepository groupRepository;

  private final UserGroupsRepository userGroupsRepo;

  @PostMapping
  public ResponseEntity<Group> createGroups(@Valid @RequestBody Group group) {
    Group savedGroups = groupRepository.save(group);
    CurrentUser currentUser = (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
    UserGroup userGroup = UserGroup
      .builder()
      .group(new Group(savedGroups.getId()))
      .userTenant(new UserTenant(currentUser.getUserTenantId()))
      .isOwner(true)
      .build();
    userGroupsRepo.save(userGroup);
    return new ResponseEntity<>(savedGroups, HttpStatus.CREATED);
  }

  @GetMapping("/count")
  public ResponseEntity<Count> countGroups() {
    Count count = Count.builder().totalCount(groupRepository.count()).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Group>> getAllGroups() {
    List<Group> groupList = groupRepository.findAll();
    return new ResponseEntity<>(groupList, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Group> getGroupById(@PathVariable("id") UUID id) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    Group group = groupRepository
      .findOne(
        GroupSpecification.byGroupIdAndTenantId(id, currentUser.getTenantId())
      )
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No group is present against given value"
        )
      );
    return new ResponseEntity<>(group, HttpStatus.OK);
  }

  @Transactional
  @PatchMapping("{id}")
  public ResponseEntity<String> updateGroupById(
    @PathVariable("id") UUID id,
    @RequestBody Group group
  ) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    Group targetGroup = groupRepository
      .findOne(
        GroupSpecification.byGroupIdAndTenantId(id, currentUser.getTenantId())
      )
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No group is present against given value"
        )
      );
    BeanUtils.copyProperties(
      group,
      targetGroup,
      CommonUtils.getNullPropertyNames(group)
    );
    groupRepository.save(targetGroup);
    return new ResponseEntity<>("Group PATCH success", HttpStatus.NO_CONTENT);
  }

  @Transactional
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteGroupById(@PathVariable("id") UUID id) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    userGroupsRepo.delete(
      UserGroupsSpecification.byGroupIdAndTenantId(
        id,
        currentUser.getTenantId()
      )
    );

    groupRepository.delete(
      GroupSpecification.byGroupIdAndTenantId(id, currentUser.getTenantId())
    );

    return new ResponseEntity<>("Groups DELETE success", HttpStatus.NO_CONTENT);
  }
}
