package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Group;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository groupRepository;

    private final UserGroupsRepository userGroupsRepo;

    @PostMapping("")
    public ResponseEntity<Object> createGroups(@Valid @RequestBody Group group) {
        Group savedGroups = groupRepository.save(group);
        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        UserGroup userGroup = UserGroup.builder().groupId(savedGroups.getId()).userTenantId(currentUser.getUserTenantId()).isOwner(true).build();
        UserGroup savedUserGroup = userGroupsRepo.save(userGroup);
        return new ResponseEntity<>(savedGroups, HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> countGroups() {

        Count count = Count.builder().count(groupRepository.count()).build();
        return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllGroups() {
        List<Group> groupList = groupRepository.findAll();
        return new ResponseEntity<Object>(groupList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getGroupById(@PathVariable("id") UUID id) {

        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No group is present against given value");
        return new ResponseEntity<Object>(group.get(), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateGroup(@PathVariable("id") UUID id, @RequestBody Group group) {
        Group target;
        Optional<Group> savedGroup = groupRepository.findById(id);
        if (savedGroup.isPresent()) {
            target = savedGroup.get();
            BeanUtils.copyProperties(group, target, CommonUtils.getNullPropertyNames(group));
            groupRepository.save(target);

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No group is present against given value");

        return new ResponseEntity<Object>("Group PATCH success", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {
        userGroupsRepo.deleteAllByGroupId(id);

        groupRepository.deleteById(id);

        return new ResponseEntity<String>("Groups DELETE success", HttpStatus.NO_CONTENT);
    }
}
