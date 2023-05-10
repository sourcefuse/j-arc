package com.sourcefuse.jarc.services.usertenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({ "classpath:message.properties" })
public class UserTenantServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserTenantServiceApplication.class, args);
  }
}
