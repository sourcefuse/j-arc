package com.sourcefuse.jarc.services.notificationservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;

@Configuration
@EnableJpaRepositories(
  repositoryBaseClass = SoftDeletesRepositoryImpl.class,
  basePackages = 
  "com.sourcefuse.jarc.services.notificationservice.repositories.softdelete"
)
public class SoftDeletesRepositoryConfig {}
