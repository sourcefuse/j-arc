package com.sourcefuse.jarc.services.notificationservice.configuration;

import com.sourcefuse.jarc.core.constants.NotificationPackageConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  basePackages = NotificationPackageConstants.JPA_REPO_PACKAGE
)
public class JpaRepositoryConfig {}
