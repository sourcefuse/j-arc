package com.sourcefuse.jarc.sandbox.featuretoggleexample.controller;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DemoController {

  /* Allowed for all System level */
  @FeatureToggle(value = "SYSTEMLEVEL")
  @GetMapping("/system-level")
  @PreAuthorize("isAuthenticated()")
  public String systemLevel() {
    return "The System Level Feature Check Worked!!!";
  }

  /* All the Users of specific tenants Allowed */
  @FeatureToggle(value = "TENANTLEVEL")
  @GetMapping("/tenant-level")
  @PreAuthorize("isAuthenticated()")
  public String tenantLevel() {
    return "The Tenant Level Feature Check Worked!!!";
  }

  /* Specific Users of all Tenants Allowed */
  /* Passing a Handler as well*/
  @FeatureToggle(
    value = "USERTENANTLEVEL",
    handler = "com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers.SimpleHandler"
  )
  @GetMapping("/user-tenant-level")
  @PreAuthorize("isAuthenticated()")
  public String userTenantLevel() {
    return "The User Tenant Level Feature Check Worked!!!";
  }

  /*Specific Users of Specific Tenants Allowed*/
  @FeatureToggle(value = "USERTENANTS")
  @GetMapping("/user-tenant")
  @PreAuthorize("isAuthenticated()")
  public String userTenants() {
    return "The Feature Check Worked!!!";
  }
}
