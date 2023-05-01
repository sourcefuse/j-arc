package com.sourcefuse.jarc.services.auditservice.audit.entitylistener;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.sourcefuse.jarc.services.auditservice.models.SoftDeleteEntity;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;

import jakarta.persistence.PreUpdate;

public class SoftDeleteEntityListner<T extends SoftDeleteEntity> {

	@PreUpdate
	public void beforeDelete(T entity) {
		Assert.notNull(entity, "Entity must not be null");
		System.out.println("delete called");
//        entity.setDeleted(true);
		if (!entity.isDeleted()) {
			entity.setDeletedBy(null);
			entity.setDeletedOn(null);
		} else {
			if (entity.isDeleted() && entity.getDeletedBy() == null) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Assert.notNull(authentication, "Forbidden :: User is not Authenticated");
				CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
				Assert.notNull(currentUser, "Current User is null can not set deleted by");
				entity.setDeletedBy(currentUser.getUser().getId());
				entity.setDeletedOn(new Date());
			}
		}
	}
}
