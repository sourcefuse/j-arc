package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.commonutils.CommonUtils;
import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleKey;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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
public class UserGroupController {

  private final GroupRepository groupRepository;

  private final UserGroupsRepository userGroupsRepo;

  @Value("${one.owner.msg}")
  private String oneOwnerMsg;

  @Value("${user.group.not.found}")
  private String userGroupNotFound;

  @Value("${only.grp.owner.access}")
  private String groupOwnerAccess;

  @PostMapping("{id}/user-groups")
  public ResponseEntity<Object> createUserGroup(
    @Valid @RequestBody UserGroup userGroup,
    @PathVariable("id") UUID id
  ) {
    Optional<UserGroup> savedUserGroup;
    Group savedGroup;
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      savedGroup = group.get();
      if (userGroup.getGroup().getId() == null) {
        userGroup.getGroup().setId(savedGroup.getId());
      }
      savedUserGroup =
        userGroupsRepo.findByGroupIdAndUserTenantId(
          userGroup.getGroup().getId(),
          userGroup.getUserTenant().getId()
        );
      if (!savedUserGroup.isPresent()) {
        savedUserGroup = (Optional.ofNullable(userGroupsRepo.save(userGroup)));
        group.get().setModifiedOn(savedUserGroup.get().getModifiedOn());
        groupRepository.save(savedGroup);
      }
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        CommonConstants.NO_GRP_PRESENT + id
      );
    }
    return new ResponseEntity<>(savedUserGroup.get(), HttpStatus.CREATED);
  }

  @Transactional
  @PatchMapping("{id}/user-groups/{userGroupId}")
  public ResponseEntity<Object> updateAll(
    @PathVariable("id") UUID id,
    @RequestBody UserGroup userGroup,
    @PathVariable("userGroupId") UUID userGroupId
  ) {
    /** INFO fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      Count count = Count
        .builder()
        .totalCount(userGroupsRepo.countByGroupIdAndIsOwner(id, true))
        .build();
      if (count.getTotalCount() == 1) {
        Optional<UserGroup> savedUserGrp =
          userGroupsRepo.findByGroupIdAndIdAndIsOwner(id, userGroupId, true);

        if (
          savedUserGrp.isPresent() && userGroup != null && !userGroup.isOwner()
        ) {
          throw new ResponseStatusException(HttpStatus.FORBIDDEN, oneOwnerMsg);
        }
      }
      userGroup.setId(null);
      UserGroup targetUserGroup = userGroupsRepo
        .findById(userGroupId)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            CommonConstants.NO_USR_GRP_PRESENT + " " + userGroupId
          )
        );
      BeanUtils.copyProperties(
        userGroup,
        targetUserGroup,
        CommonUtils.getNullPropertyNames(userGroup)
      );
      userGroupsRepo.save(targetUserGroup);

      group.get().setModifiedOn(LocalDateTime.now());
      groupRepository.save(group.get());
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        CommonConstants.NO_GRP_PRESENT
      );
    }
    return new ResponseEntity<>(
      "Group.UserGroup PATCH success count",
      HttpStatus.OK
    );
  }

  @Transactional
  @DeleteMapping("{id}/user-groups/{userGroupId}")
  public ResponseEntity<Object> deleteUsrGrp(
    @PathVariable("id") UUID id,
    @PathVariable("userGroupId") UUID userGroupId
  ) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
    /** INFO fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      UUID userTenantId = currentUser.getUserTenantId();
      List<UserGroup> savedUserGroup =
        userGroupsRepo.findByGroupIdOrIdOrUserTenantId(
          id,
          userGroupId,
          userTenantId
        );
      UserGroup userGroup = savedUserGroup
        .stream()
        .filter(userGrp -> userGrp.getId().equals(userGroupId))
        .findFirst()
        .orElseThrow(() ->
          new ResponseStatusException(HttpStatus.FORBIDDEN, userGroupNotFound)
        );
      extracted(currentUser, userTenantId, savedUserGroup, userGroup);
      Count count = Count
        .builder()
        .totalCount(userGroupsRepo.countByGroupId(id))
        .build();
      if (count.getTotalCount() == 1) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, oneOwnerMsg);
      }
      userGroupsRepo.deleteById(userGroupId);
      group.get().setModifiedOn(LocalDateTime.now());
      groupRepository.save(group.get());
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        CommonConstants.NO_GRP_PRESENT
      );
    }
    return new ResponseEntity<>("UserGroup DELETE success", HttpStatus.OK);
  }

  private static void extracted(
    IAuthUserWithPermissions currentUser,
    UUID usrTenantId,
    List<UserGroup> userGroup,
    UserGroup userGroupRecord
  ) {
    boolean isAdmin = currentUser.getRole() == RoleKey.ADMIN.toString();
    Optional<UserGroup> firstCurrentUserGroup = userGroup
      .stream()
      .filter(userGrp -> userGrp.getUserTenant().getId().equals(usrTenantId))
      .findFirst();
    UserGroup currentUserGroup = new UserGroup();
    if (firstCurrentUserGroup.isPresent()) {
      currentUserGroup = firstCurrentUserGroup.get();
    }
    if (
      !(
        isAdmin ||
        (currentUserGroup.isOwner()) ||
        (
          userGroupRecord
            .getUserTenant()
            .getId()
            .equals(currentUser.getUserTenantId())
        )
      )
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "Only group owners can access"
      );
    }

    if (
      userGroupRecord
        .getUserTenant()
        .getId()
        .equals(currentUser.getUserTenantId()) &&
      (currentUserGroup.isOwner())
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "An owner cannot remove himself as the owner"
      );
    }
  }

  @GetMapping("{id}/user-groups")
  public ResponseEntity<Object> getAllUsTenantByRole(
    @PathVariable("id") UUID id
  ) {
    /** INFO fetch value in Group against primary key also a validation
         if group table does not have the records then  it will also not be
         available in userGroups table */
    List<UserGroup> userGroupList;
    groupRepository
      .findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "No group is present against given value"
        )
      );
    userGroupList = userGroupsRepo.findAllByGroupId(id);
    return new ResponseEntity<>(userGroupList, HttpStatus.OK);
  }

  @GetMapping("{id}/user-groups/count")
  public ResponseEntity<Object> countUserGroup(@PathVariable("id") UUID id) {
    Long groupCount = userGroupsRepo.countByGroupId(id);
    Count count = Count.builder().totalCount(groupCount).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }
}
