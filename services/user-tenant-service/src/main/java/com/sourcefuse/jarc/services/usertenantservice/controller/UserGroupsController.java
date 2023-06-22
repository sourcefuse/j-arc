package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.dtos.CountResponse;
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

  private final UserGroupsRepository userGroupsRepository;

  @GetMapping
  public ResponseEntity<List<UserGroup>> fetchAllUserGroups() {
    return new ResponseEntity<>(userGroupsRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping("/count")
  public ResponseEntity<CountResponse> countTenants() {
    List<UserGroup> userGroupsList = userGroupsRepository.findAll();
    return new ResponseEntity<>(
      CountResponse.builder().count((long) userGroupsList.size()).build(),
      HttpStatus.OK
    );
  }
}
