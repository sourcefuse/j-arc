package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

  @GetMapping("/restricted")
  @PreAuthorize("isAuthenticated() && hasAuthority('ViewRoles')")
  // @PostAuthorize("")
  public String restricted() {
    return "Authorized to access!";
  }
}
