package com.sourcefuse.jarc.services.usertenantservice.config;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  repositoryBaseClass = SoftDeletesRepositoryImpl.class,
  basePackages = CommonConstants.CORE_SOFT_DELETE_PACKAGE
)
public class SoftDeletesConfig {}
