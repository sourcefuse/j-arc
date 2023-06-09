package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.GroupSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserGroupsSpecification;
import com.sourcefuse.jarc.services.usertenantservice.utils.CurrentUserUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    Group savedGroup = groupRepository
      .findById(groupId)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_GRP_PRESENT + groupId
        )
      );
    if (!userGroup.getGroup().getId().equals(savedGroup.getId())) {
      userGroup.getGroup().setId(savedGroup.getId());
    }
    /*** INFO :check for mismatch tenantId*/
    if (!savedGroup.getTenantId().equals(userGroup.getTenantId())) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "tenantId in Request does not match with existing group" + "tenantId"
      );
    }
    Optional<UserGroup> savedUserGroup;
    savedUserGroup =
      userGroupsRepo.findOne(
        UserGroupsSpecification.byGroupIdAndUserTenantId(
          userGroup.getGroup().getId(),
          userGroup.getUserTenant().getId()
        )
      );
    if (!savedUserGroup.isPresent()) {
      savedUserGroup = (Optional.ofNullable(userGroupsRepo.save(userGroup)));
      savedGroup.setModifiedOn(savedUserGroup.get().getModifiedOn());
      groupRepository.save(savedGroup);
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
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    Group group = groupRepository
      .findOne(
        GroupSpecification.byGroupIdAndTenantId(
          groupId,
          currentUser.getTenantId()
        )
      )
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_GRP_PRESENT
        )
      );
    CountResponse count = CountResponse
      .builder()
      .count(
        userGroupsRepo.count(
          UserGroupsSpecification.byGroupIdAndIsOwner(group.getId(), true)
        )
      )
      .build();
    if (count.getCount() == 1) {
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
    UserGroup targetUserGroup = getUserGroup(userGroupId, currentUser);
    BeanUtils.copyProperties(
      userGroup,
      targetUserGroup,
      CommonUtils.getNullPropertyNames(userGroup)
    );
    userGroupsRepo.save(targetUserGroup);

    group.setModifiedOn(LocalDateTime.now());
    groupRepository.save(group);
  }

  private UserGroup getUserGroup(UUID userGroupId, CurrentUser currentUser) {
    return userGroupsRepo
      .findOne(
        UserGroupsSpecification.byUserGroupIdAndTenantId(
          userGroupId,
          currentUser.getTenantId()
        )
      )
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_USR_GRP_PRESENT + " " + userGroupId
        )
      );
  }

  @Override
  public void deleteUserGroup(UUID groupId, UUID userGroupId) {
    CurrentUser currentUser = CurrentUserUtils.getCurrentUser();
    /** INFO fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Group group = groupRepository
      .findOne(
        GroupSpecification.byGroupIdAndTenantId(
          groupId,
          currentUser.getTenantId()
        )
      )
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          CommonConstants.NO_GRP_PRESENT
        )
      );
    UUID userTenantId = currentUser.getUserTenantId();
    List<UserGroup> savedUserGroup = userGroupsRepo.findAll(
      UserGroupsSpecification.byGroupIdOrIdOrUserTenantId(
        group.getId(),
        userGroupId,
        userTenantId
      )
    );
    UserGroup userGroup = getUserGroup(userGroupId, savedUserGroup);
    checkGroupOwnerAccessPermission(
      currentUser,
      userTenantId,
      savedUserGroup,
      userGroup
    );
    CountResponse count = CountResponse
      .builder()
      .count(userGroupsRepo.count(UserGroupsSpecification.byGroupId(groupId)))
      .build();
    if (count.getCount() == 1) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, oneOwnerMsg);
    }
    userGroupsRepo.delete(
      UserGroupsSpecification.byUserGroupIdAndTenantId(
        userGroupId,
        currentUser.getTenantId()
      )
    );
    group.setModifiedOn(LocalDateTime.now());
    groupRepository.save(group);
  }

  private UserGroup getUserGroup(
    UUID userGroupId,
    List<UserGroup> savedUserGroup
  ) {
    return savedUserGroup
      .stream()
      .filter(userGrp -> userGrp.getId().equals(userGroupId))
      .findFirst()
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.FORBIDDEN, userGroupNotFound)
      );
  }

  private static void checkGroupOwnerAccessPermission(
    CurrentUser currentUser,
    UUID usrTenantId,
    List<UserGroup> userGroup,
    UserGroup userGroupRecord
  ) {
    boolean isAdmin = currentUser
      .getRoleType()
      .toString()
      .equals(RoleKey.ADMIN.toString());
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
