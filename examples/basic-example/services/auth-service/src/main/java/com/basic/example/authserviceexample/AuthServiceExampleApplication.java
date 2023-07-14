package com.basic.example.authserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.sourcefuse.jarc.services.authservice" ,"com.basic.example.authserviceexample"})
public class AuthServiceExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceExampleApplication.class, args);
	}

}
