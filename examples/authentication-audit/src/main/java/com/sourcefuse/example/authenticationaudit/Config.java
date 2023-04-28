package com.sourcefuse.example.authenticationaudit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.services.auditservice.audit.softdelete.SoftDeletesRepositoryImpl;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class,basePackages = {"com.sourcefuse.example.authenticationaudit.repositories"})
public class Config {

}
