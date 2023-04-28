package com.sourcefuse.example.authenticationaudit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({ "com.sourcefuse.example.authenticationaudit", "com.sourcefuse.jarc.services.auditservice",
		"com.sourcefuse.jarc.services.authservice" })
@EnableJpaRepositories(basePackages = {"com.sourcefuse.jarc.services.authservice.repositories"})
public class AuthenticationAuditApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAuditApplication.class, args);
	}

}
