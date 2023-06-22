package com.sourcefuse.jarc.authlib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.sourcefuse.jarc.core", "com.sourcefuse.jarc.authlib" })
public class AuthLibApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthLibApplication.class, args);
  }
}
