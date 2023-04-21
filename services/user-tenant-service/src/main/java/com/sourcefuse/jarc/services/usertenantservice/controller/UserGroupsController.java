package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.service.UserGroupsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = {"${api.user-group.context.url}"})
@RequiredArgsConstructor

public class UserGroupsController {
    private final UserGroupsService userGroupsService;

    @GetMapping()
    public ResponseEntity<Object> fetchAllUserGroups() {
        log.info("::::::::::::: Fetch all the User Groups present :::::::::::::;");
        List<UserGroup> UserGroupsList = userGroupsService.findAll();
        return new ResponseEntity<Object>(UserGroupsList, HttpStatus.OK);
    }

    //doubt::
    @GetMapping(value = {"${api.user-group.count.context.url}"})
    public ResponseEntity<Object> countTenants() {
        log.info("::::::::::::: Count & Fetch all the User Groups present :::::::::::::;");
        List<UserGroup> UserGroupsList = userGroupsService.findAll();
        return new ResponseEntity<Object>(UserGroupsList, HttpStatus.OK);
    }

}
