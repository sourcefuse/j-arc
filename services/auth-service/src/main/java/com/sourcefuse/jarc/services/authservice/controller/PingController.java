package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PingController {

  @GetMapping("/ping")
  // @RolesAllowed("user")
  public String ping() {
    return "Pong!";
  }
}
