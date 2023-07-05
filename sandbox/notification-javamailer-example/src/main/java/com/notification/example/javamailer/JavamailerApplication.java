package com.notification.example.javamailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  {
    "com.notification.example.javamailer",
    "com.sourcefuse.jarc.services.notificationservice"
  }
)
public class JavamailerApplication {

  public static void main(String[] args) {
    SpringApplication.run(JavamailerApplication.class, args);
  }
}
