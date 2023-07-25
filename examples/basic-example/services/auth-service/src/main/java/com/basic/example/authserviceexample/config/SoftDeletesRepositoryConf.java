package com.basic.example.authserviceexample.config;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  repositoryBaseClass = SoftDeletesRepositoryImpl.class,
  basePackages = 
  "com.basic.example.authserviceexample.repository"
)
public class SoftDeletesRepositoryConf {}

