package com.basic.example.notificationserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.basic.example.notificationserviceexample", "com.sourcefuse.jarc.services.notificationservice" })
public class NotificationServiceExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceExampleApplication.class, args);
	}

}
