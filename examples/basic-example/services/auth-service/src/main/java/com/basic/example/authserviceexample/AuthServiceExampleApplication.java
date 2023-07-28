package com.basic.example.authserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.basic.example.authserviceexample", "com.sourcefuse.jarc.services.authservice"})
public class AuthServiceExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceExampleApplication.class, args);
	}
}
