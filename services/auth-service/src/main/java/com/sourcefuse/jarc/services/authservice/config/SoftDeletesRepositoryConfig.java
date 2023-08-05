package com.sourcefuse.jarc.services.authservice.config;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  repositoryBaseClass = SoftDeletesRepositoryImpl.class,
  basePackages =
   "com.sourcefuse.jarc.services.authservice.repositories"
)
public class SoftDeletesRepositoryConfig {}
