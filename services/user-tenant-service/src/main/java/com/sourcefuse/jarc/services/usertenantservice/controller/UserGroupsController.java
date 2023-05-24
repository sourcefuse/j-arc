package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dto.Count;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user-groups")
@RequiredArgsConstructor
public class UserGroupsController {

  private final UserGroupsRepository userGroupsRepo;

  @GetMapping
  public ResponseEntity<Object> fetchAllUserGroups() {
    List<UserGroup> userGroupsList = userGroupsRepo.findAll();
    return new ResponseEntity<>(userGroupsList, HttpStatus.OK);
  }

  //doubt::
  @GetMapping("/count")
  public ResponseEntity<Object> countTenants() {
    List<UserGroup> userGroupsList = userGroupsRepo.findAll();
    return new ResponseEntity<>(
      Count.builder().totalCnt((long) userGroupsList.size()).build(),
      HttpStatus.OK
    );
  }
}
