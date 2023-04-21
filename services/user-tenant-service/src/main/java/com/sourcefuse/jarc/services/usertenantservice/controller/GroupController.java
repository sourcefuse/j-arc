package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Group;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.GenericRespBuilder;
import com.sourcefuse.jarc.services.usertenantservice.service.GroupService;
import com.sourcefuse.jarc.services.usertenantservice.service.UserGroupsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = {"${api.groups.context.url}"})
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final UserGroupsService userGroupsService;

    private final GenericRespBuilder genericRespBuilder;


    @PostMapping("")
    public ResponseEntity<Object> createGroups(@Valid @RequestBody Group group) {
        log.info(" ::::::::::::  Group Apis consumed for creating Groups::::::::::::");
        Group savedGroups = groupService.save(group);
        log.info(" ::::::::::::  Groups Created ,Now Creating UserGroup based on groupID " + savedGroups.getId() +
                "userTenantId" + "isOwner=true");
        /***userTenantId: this.currentUser.userTenantId, this has to done TODO doubt::*/
        UserGroup userGroup = UserGroup.builder().groupId(savedGroups.getId()).userTenantId(savedGroups.getId()).isOwner(true).build();

        UserGroup savedUserGroup = userGroupsService.save(userGroup);
        log.info(" ::::::::::::  User groups created for ID::::::" + savedUserGroup.getId() + " groupID" + savedUserGroup.getGroupId());

        return new ResponseEntity<>(savedGroups, HttpStatus.CREATED);
    }

    @GetMapping(value = {"${api.groups.count}"})
    public ResponseEntity<Object> countGroups() {

        log.info("::::::::::::: Groups  count rest Apis consumed :::::::::::::;");

        Count count = Count.builder().count(groupService.count()).build();
        return new ResponseEntity<Object>(count, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllGroups() {

        log.info(":::::::::::::   Rest Apis consumed to get total Groups -- present:::::::::::::;");
        List<Group> groupList = groupService.findAll();
        return new ResponseEntity<Object>(groupList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getGroupById(@PathVariable("id") UUID id) throws ApiPayLoadException, Exception {

        log.info("::::::::::::: Fetch Groups based against id:::::::::::" + id);
        Group group = groupService.findById(id).get();
        return new ResponseEntity<Object>(group, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateGroup(@PathVariable("id") UUID id, @RequestBody Group group) throws ApiPayLoadException {

        log.info("Group update rest Apis consumed based on id " + id);
        Group savedGroup = groupService.findById(id).get();
        //  updateTenant.setAddress(tenant.getAddress());
        log.info(savedGroup.toString());

        groupService.update(group, savedGroup);
        return new ResponseEntity<Object>("Group PATCH success", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRolesById(@PathVariable("id") UUID id) {
        log.info("::::::::::::: Delete rest Apis consumed for Group against Id:::::::" + id);

        userGroupsService.deleteAllByGroupId(id);

        groupService.deleteById(id);

        return new ResponseEntity<String>("Groups DELETE success", HttpStatus.NO_CONTENT);
    }
}
