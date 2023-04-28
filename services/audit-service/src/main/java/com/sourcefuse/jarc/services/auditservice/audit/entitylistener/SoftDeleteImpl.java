package com.sourcefuse.jarc.services.auditservice.audit.entitylistener;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SoftDeleteImpl<ID> {
	@Query("UPDATE #{#entityName} e SET e.deleted = true WHERE e.id = :id")
	@Modifying
	@Transactional
	public abstract void softDeleteById(@Param("id") ID id);
}
