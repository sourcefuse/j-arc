package com.sourcefuse.jarc.services.auditservice.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.jarc.services.auditservice.models.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, UUID> {

}
