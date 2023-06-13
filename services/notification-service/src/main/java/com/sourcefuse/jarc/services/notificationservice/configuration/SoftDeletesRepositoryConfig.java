package com.sourcefuse.jarc.services.notificationservice.configuration;

import com.sourcefuse.jarc.core.constants.NotificationPackageConstants;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  repositoryBaseClass = SoftDeletesRepositoryImpl.class,
  basePackages = NotificationPackageConstants.SOFTDELETE_REPO_PACKAGE
)
public class SoftDeletesRepositoryConfig {}
