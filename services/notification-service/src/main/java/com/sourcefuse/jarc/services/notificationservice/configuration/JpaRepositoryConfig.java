package com.sourcefuse.jarc.services.notificationservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  basePackages = "com.sourcefuse.jarc.services.notificationservice.repositories.simple"
)
public class JpaRepositoryConfig {}
