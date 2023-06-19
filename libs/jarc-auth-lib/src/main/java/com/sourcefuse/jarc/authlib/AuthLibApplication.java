package com.sourcefuse.jarc.authlib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  { AuthLibApplication.CORE_PACKAGE, AuthLibApplication.AUTH_LIB_PACKAGE, }
)
public class AuthLibApplication {
  public static final String CORE_PACKAGE = "com.sourcefuse.jarc.core";
  public static final String AUTH_LIB_PACKAGE = "com.sourcefuse.jarc.authlib";

  public static void main(String[] args) {
    SpringApplication.run(AuthLibApplication.class, args);
  }
}
