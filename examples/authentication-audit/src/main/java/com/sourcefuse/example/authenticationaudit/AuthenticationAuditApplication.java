package com.sourcefuse.example.authenticationaudit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({ "com.sourcefuse.jarc.services.auditservice", "com.sourcefuse.example.authenticationaudit",
		"com.sourcefuse.jarc.services.authservice" })
public class AuthenticationAuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAuditApplication.class, args);
	}

}
