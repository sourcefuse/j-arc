package com.sourcefuse.jarc.services.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.authservice.dtos.LoginDto;
import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import com.sourcefuse.jarc.services.authservice.providers.ClientPasswordVerifyProvider;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MyController {
  private final ClientPasswordVerifyProvider clientPasswordVerifyProvider;

  @GetMapping("/restricted")
  @PreAuthorize("isAuthenticated() && hasAuthority('ViewRoles')")
  // @PostAuthorize("")
  public String restricted() {
    return "Authorized to access!";
  }
  @PostMapping(value = { "/login" })
  public AuthClient login(
    @Valid @RequestBody LoginDto loginDto
  ) {
    AuthClient client =
    this.clientPasswordVerifyProvider.value(
        loginDto.getClientId(),
        loginDto.getClientSecret()
      );
    
    return client;
  }

}
