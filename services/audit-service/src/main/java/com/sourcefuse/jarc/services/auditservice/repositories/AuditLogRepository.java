package com.sourcefuse.jarc.services.auditservice.repositories;

import com.sourcefuse.jarc.services.auditservice.models.AuditLog;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog, UUID> {}
