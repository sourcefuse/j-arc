package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
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
@RequestMapping("/user-groups")
@RequiredArgsConstructor

public class UserGroupsController {

    private final UserGroupsRepository userGroupsRepo;

    @GetMapping()
    public ResponseEntity<Object> fetchAllUserGroups() {
        List<UserGroup> UserGroupsList = userGroupsRepo.findAll();
        return new ResponseEntity<Object>(UserGroupsList, HttpStatus.OK);
    }

    //doubt::
    @GetMapping("/count")
    public ResponseEntity<Object> countTenants() {
        List<UserGroup> UserGroupsList = userGroupsRepo.findAll();
        return new ResponseEntity<Object>(UserGroupsList, HttpStatus.OK);
    }

}
