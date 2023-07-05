package com.notification.example.pubnub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
  {
    "com.notification.example.pubnub",
    "com.sourcefuse.jarc.services.notificationservice"
  }
)
public class PubnubApplication {

  public static void main(String[] args) {
    SpringApplication.run(PubnubApplication.class, args);
  }
}
