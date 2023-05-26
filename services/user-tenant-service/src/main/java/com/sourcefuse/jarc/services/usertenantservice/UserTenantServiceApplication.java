package com.sourcefuse.jarc.services.usertenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({ "classpath:message.properties" })
@ComponentScan(
  basePackages = {
    "com.sourcefuse.jarc.services.usertenantservice",
    "com.sourcefuse.jarc.core",
  }
)
@EntityScan(
  basePackages = {
    "com.sourcefuse.jarc.services.usertenantservice.dto",
    "com.sourcefuse.jarc.core.models",
  }
)
public class UserTenantServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserTenantServiceApplication.class, args);
  }
}
