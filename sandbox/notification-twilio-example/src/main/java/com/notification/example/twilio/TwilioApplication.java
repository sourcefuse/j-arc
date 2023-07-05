package com.notification.example.twilio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  {
    "com.notification.example.twilio",
    "com.sourcefuse.jarc.services.notificationservice"
  }
)
public class TwilioApplication {

  public static void main(String[] args) {
    SpringApplication.run(TwilioApplication.class, args);
  }
}
