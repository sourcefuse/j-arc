package com.sourcefuse.jarc.services.auditservice.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.services.auditservice.audit.mixin.AuditLogRepositoryMixin;

@Configuration
@EnableJpaRepositories(basePackages = {
		"com.sourcefuse.jarc.services.auditservice.audit.repositories" }, repositoryBaseClass = AuditLogRepositoryMixin.class)
public class AuditConfiguration {

}
