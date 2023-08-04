package com.basic.example.usertenantserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  {
    "com.basic.example.usertenantserviceexample",
    "com.sourcefuse.jarc.services.usertenantservice",
  }
)
public class UserTenantServiceExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserTenantServiceExampleApplication.class, args);
  }
}
