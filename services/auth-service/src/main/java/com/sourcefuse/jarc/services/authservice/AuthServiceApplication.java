package com.sourcefuse.jarc.services.authservice;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
  { AuthServiceApplication.CORE_PACKAGE, AuthServiceApplication.AUTH_LIB_PACKAGE, AuthServiceApplication.AUTH_PACKAGE }
)
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class AuthServiceApplication {

  public static final String CORE_PACKAGE = "com.sourcefuse.jarc.core";
  public static final String AUTH_LIB_PACKAGE = "com.sourcefuse.jarc.authlib";
  public static final String AUTH_PACKAGE =
    "com.sourcefuse.jarc.services.authservice";

  public static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }
}
