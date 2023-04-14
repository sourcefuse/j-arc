package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
public class MyController {

  @GetMapping("/restricted")
  public String restricted() {
    return "Authorized to access!";
  }
}
