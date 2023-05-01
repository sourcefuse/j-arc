package com.sourcefuse.jarc.services.auditservice.audit.mixin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.sourcefuse.jarc.services.auditservice.models.BaseEntity;


@NoRepositoryBean
public interface AuditLogRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

}
