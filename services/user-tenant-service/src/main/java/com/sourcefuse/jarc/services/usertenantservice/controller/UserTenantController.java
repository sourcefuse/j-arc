package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.service.UserTenantService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user-tenants")
@RequiredArgsConstructor
public class UserTenantController {

  private final UserTenantService userTenantService;

  @GetMapping("{id}")
  public ResponseEntity<UserView> getUserTenantById(
    @PathVariable("id") UUID id
  ) {
    /*** INFO :As discussed by samarpan currently
     checkViewTenantRestrictedPermissions we
     dont have to implement..
     One tenant cannot see others tenant data ***/
    UserView userView = userTenantService.getUserTenantById(id);
    return new ResponseEntity<>(userView, HttpStatus.OK);
  }
}
