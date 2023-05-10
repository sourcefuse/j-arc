package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleKey;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/groups")
@RequiredArgsConstructor
public class UserGroupController {

  private final GroupRepository groupRepository;

  private final UserGroupsRepository userGroupsRepo;

  @PostMapping("{id}" + "/user-groups")
  public ResponseEntity<Object> createRole(
    @Valid @RequestBody UserGroup userGroup,
    @PathVariable("id") UUID id
  ) {
    UserGroup usrGr;
    Group svdGroup;
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      svdGroup = group.get();
      if (userGroup.getGroupId() == null) {
        userGroup.setGroupId(svdGroup.getId());
      }

      usrGr =
        userGroupsRepo
          .findByGroupIdAndUserTenantId(
            userGroup.getGroupId(),
            userGroup.getUserTenantId()
          )
          .get();
      if (usrGr == null) {
        usrGr = (userGroupsRepo.save(userGroup));
        group.get().setModifiedOn(usrGr.getModifiedOn());
        groupRepository.save(group.get());
      }
    } else throw new ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "No group is present against given value" + id
    );
    return new ResponseEntity<>(usrGr, HttpStatus.CREATED);
  }

  @PatchMapping("{id}" + "/user-groups" + "{userGroupId}")
  public ResponseEntity<Object> updateAll(
    @PathVariable("id") UUID id,
    @RequestBody UserGroup userGroup,
    @PathVariable("userGroupId") UUID userGroupId
  ) {
    /** fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      Count count = Count
        .builder()
        .totalCnt(userGroupsRepo.countByGroupIdAndIsOwner(id, true))
        .build();
      if (count.getTotalCnt() == 1) {
        Optional<UserGroup> usrGrp =
          userGroupsRepo.findByGroupIdAndIdAndIsOwner(id, userGroupId, true);

        if (
          usrGrp.isPresent() && !usrGrp.get().isOwner()
        ) throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "${one.owner.msg}"
        );
      }
      if (userGroup.getId() == null) {
        userGroup.setId(userGroupId);
      }
      userGroupsRepo.save(userGroup);

      group.get().setModifiedOn(new Date());
      groupRepository.save(group.get());
    } else throw new ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "No group is present against given value"
    );

    return new ResponseEntity<>(
      "Group.UserGroup PATCH success count",
      HttpStatus.OK
    );
  }

  @DeleteMapping("{id}" + "/user-groups" + "{userGroupId}")
  public ResponseEntity<Object> deleteUsrGrp(
    @PathVariable("id") UUID id,
    @PathVariable("userGroupId") UUID userGroupId
  ) {
    IAuthUserWithPermissions currentUser =
      (IAuthUserWithPermissions) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    /** fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      UUID usrTenantId = currentUser.getUserTenantId();
      List<UserGroup> usrGrp = userGroupsRepo.findByGroupIdOrIdOrUserTenantId(
        id,
        userGroupId,
        usrTenantId
      );

      Optional<UserGroup> userGroupRecord = usrGrp
        .stream()
        .filter(usrGp -> usrGp.getId() == userGroupId)
        .findFirst();

      if (!userGroupRecord.isPresent()) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "${user.group.not.found}"
      );

      /** TODO::
       * Auth logic and Admin persion check logic pending
       * */
      boolean isAdmin =
        Integer.parseInt(currentUser.getRole()) == RoleKey.ADMIN.getValue();
      Optional<UserGroup> currentUserGroup = usrGrp
        .stream()
        .filter(usrGp -> usrGp.getUserTenantId() == usrTenantId)
        .findFirst();
      if (
        !(
          isAdmin ||
          (currentUserGroup.isPresent() && currentUserGroup.get().isOwner()) ||
          (
            userGroupRecord.isPresent() &&
            userGroupRecord
              .get()
              .getUserTenantId()
              .equals(currentUser.getUserTenantId())
          )
        )
      ) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "${only.grp.owner.access}"
        );
      }

      if (
        userGroupRecord.isPresent() &&
        userGroupRecord
          .get()
          .getUserTenantId()
          .equals(currentUser.getUserTenantId()) &&
        (currentUserGroup.isPresent() && currentUserGroup.get().isOwner())
      ) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "${owner.cannot.remove.himself}"
        );
      }

      Count count = Count
        .builder()
        .totalCnt(userGroupsRepo.getUserGroupCountByGroupId(id))
        .build();
      if (count.getTotalCnt() == 1) {
        throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "${one.owner.msg"
        );
      }
      userGroupsRepo.deleteById(userGroupId);

      group.get().setModifiedOn(new Date());
      groupRepository.save(group.get());
    } else throw new ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "No group is present against given value"
    );

    return new ResponseEntity<>("UserGroup DELETE success", HttpStatus.OK);
  }

  @GetMapping("{id}" + "/user-groups")
  public ResponseEntity<Object> getAllUsTenantByRole(
    @PathVariable("id") UUID id
  ) {
    /** fetch value in Group against primary key also a validation
         if group table does not have the records then  it will also not be
         available in userGroups table */
    List<UserGroup> usrGrpList;
    Optional<Group> group = groupRepository.findById(id);
    if (group.isPresent()) {
      usrGrpList = userGroupsRepo.findAll();
    } else throw new ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "No group is present against given value"
    );
    return new ResponseEntity<>(usrGrpList, HttpStatus.OK);
  }

  @GetMapping("{id}" + "/user-groups/count")
  public ResponseEntity<Object> countUserGroup(@PathVariable("id") UUID id) {
    Long grpCnt = userGroupsRepo.getUserGroupCountByGroupId(id);
    Count count = Count.builder().totalCnt(grpCnt).build();
    return new ResponseEntity<>(count, HttpStatus.OK);
  }
}
