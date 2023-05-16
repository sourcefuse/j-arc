package com.sourcefuse.jarc.services.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;

@SpringBootApplication
@ComponentScan({ "com.sourcefuse.jarc.services.authservice", "com.sourcefuse.jarc.core" })
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class AuthServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }
}
