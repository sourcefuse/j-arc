package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserGroupsSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

  private final GroupRepository groupRepository;

  private final UserGroupsRepository userGroupsRepo;

  @Value("${one.owner.msg}")
  private String oneOwnerMsg;

  @Value("${user.group.not.found}")
  private String userGroupNotFound;

  @Value("${only.grp.owner.access}")
  private String groupOwnerAccess;

  @Override
  public UserGroup createUserGroup(UserGroup userGroup, UUID groupId) {
    Optional<UserGroup> savedUserGroup;
    Group savedGroup;
    Optional<Group> group = groupRepository.findById(groupId);
    if (group.isPresent()) {
      savedGroup = group.get();
      if (!userGroup.getGroup().getId().equals(savedGroup.getId())) {
        userGroup.getGroup().setId(savedGroup.getId());
      }
      savedUserGroup =
        userGroupsRepo.findOne(
          UserGroupsSpecification.byGroupIdAndUserTenantId(
            userGroup.getGroup().getId(),
            userGroup.getUserTenant().getId()
          )
        );
      if (!savedUserGroup.isPresent()) {
        savedUserGroup = (Optional.ofNullable(userGroupsRepo.save(userGroup)));
        group.get().setModifiedOn(savedUserGroup.get().getModifiedOn());
        groupRepository.save(savedGroup);
      }
    } else {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        CommonConstants.NO_GRP_PRESENT + groupId
      );
    }
    return savedUserGroup.get();
  }

  @Override
  public void updateAllUserGroup(
    UUID groupId,
    UserGroup userGroup,
    UUID userGroupId
  ) {
    /** INFO fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Optional<Group> group = groupRepository.findById(groupId);
    if (group.isPresent()) {
      Count count = Count
        .builder()
        .totalCount(
          userGroupsRepo.count(
            UserGroupsSpecification.byGroupIdAndIsOwner(groupId, true)
          )
        )
        .build();
      if (count.getTotalCount() == 1) {
        Optional<UserGroup> savedUserGrp = userGroupsRepo.findOne(
          UserGroupsSpecification.byGroupIdAndIdAndIsOwner(
            groupId,
            userGroupId,
            true
          )
        );
        if (savedUserGrp.isPresent() && !userGroup.isOwner()) {
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
  }

  @Override
  public void deleteUserGroup(UUID groupId, UUID userGroupId) {
    CurrentUser currentUser = (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();
    /** INFO fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Optional<Group> group = groupRepository.findById(groupId);
    if (group.isPresent()) {
      UUID userTenantId = currentUser.getUserTenantId();
      List<UserGroup> savedUserGroup = userGroupsRepo.findAll(
        UserGroupsSpecification.byGroupIdOrIdOrUserTenantId(
          groupId,
          userGroupId,
          userTenantId
        )
      );
      UserGroup userGroup = savedUserGroup
        .stream()
        .filter(userGrp -> userGrp.getId().equals(userGroupId))
        .findFirst()
        .orElseThrow(() ->
          new ResponseStatusException(HttpStatus.FORBIDDEN, userGroupNotFound)
        );
      checkGroupOwnerAccessPermission(currentUser, userTenantId, savedUserGroup, userGroup);
      Count count = Count
        .builder()
        .totalCount(
          userGroupsRepo.count(UserGroupsSpecification.byGroupId(groupId))
        )
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
  }

  private static void checkGroupOwnerAccessPermission(
    CurrentUser currentUser,
    UUID usrTenantId,
    List<UserGroup> userGroup,
    UserGroup userGroupRecord
  ) {
    boolean isAdmin =
      currentUser.getRoleType().toString().equals(RoleKey.ADMIN.toString());
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
}
