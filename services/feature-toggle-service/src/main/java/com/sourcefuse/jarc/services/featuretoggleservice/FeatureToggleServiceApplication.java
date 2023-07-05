package com.sourcefuse.jarc.services.featuretoggleservice;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
  {
    "com.sourcefuse.jarc.services.featuretoggleservice",
    "com.sourcefuse.jarc.core",
    "com.sourcefuse.jarc.services.authservice"
  }
)
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class FeatureToggleServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FeatureToggleServiceApplication.class, args);
  }
}
