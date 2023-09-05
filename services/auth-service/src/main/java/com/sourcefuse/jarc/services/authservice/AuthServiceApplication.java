package com.sourcefuse.jarc.services.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  {
    "com.sourcefuse.jarc.core",
    "com.sourcefuse.jarc.authlib",
    "com.sourcefuse.jarc.services.authservice"
  }
)
public class AuthServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }
}
