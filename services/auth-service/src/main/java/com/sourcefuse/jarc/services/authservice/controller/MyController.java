package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

  @GetMapping("/restricted")
  // @RolesAllowed("user")
  public String restricted() {
    return "Authorized to access!";
  }
}
