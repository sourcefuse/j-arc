package com.sourcefuse.jarc.services.auditservice.audit.entitylistener;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sourcefuse.jarc.services.auditservice.models.SoftDeleteEntity;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;

import jakarta.persistence.PreUpdate;

public class SoftDeleteEntityListner<T extends SoftDeleteEntity> {

	@PreUpdate
	public void beforeDelete(T entity) {
		System.out.println("delete called");
//        entity.setDeleted(true);
		if (!entity.isDeleted()) {
			entity.setDeletedBy(null);
			entity.setDeletedOn(null);
		} else {
			if (entity.isDeleted() && entity.getDeletedBy() == null) {
				CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				entity.setDeletedBy(currentUser.getUser().getId());
				entity.setDeletedOn(new Date());
			}
		}
	}
}
