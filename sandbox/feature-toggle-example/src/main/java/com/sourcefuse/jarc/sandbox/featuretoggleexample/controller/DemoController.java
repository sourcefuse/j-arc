package com.sourcefuse.jarc.sandbox.featuretoggleexample.controller;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureToggle;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DemoController {

  @FeatureToggle(
    value = "HELLO",
    handler = "com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers.HelloHandler"
  )
  @GetMapping("/hello")
  @PreAuthorize("isAuthenticated()")
  public String sayHello() {
    return "hellohellohello";
  }

  @FeatureToggle(value = "HI")
  @GetMapping("/hi")
  @PreAuthorize("isAuthenticated()")
  public String sayHi() {
    return "hihihi";
  }

  @FeatureToggle(
    value = "HEY",
    handler = "com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers.HeyHandler"
  )
  @GetMapping("/hey")
  @PreAuthorize("isAuthenticated()")
  public String sayHey() {
    return "heyheyhey";
  }
}