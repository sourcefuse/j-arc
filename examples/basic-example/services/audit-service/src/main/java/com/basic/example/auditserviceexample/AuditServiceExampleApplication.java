package com.basic.example.auditserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  {
    "com.basic.example.auditserviceexample",
    "com.sourcefuse.jarc.services.auditservice"
  }
)
public class AuditServiceExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuditServiceExampleApplication.class, args);
  }
}
