package com.sourcefuse.jarc.core.entitylisteners;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import com.sourcefuse.jarc.core.models.session.CurrentUser;

import jakarta.persistence.PreUpdate;

public class SoftDeleteEntityListner<T extends SoftDeleteEntity> {

	@PreUpdate
	public void beforeDelete(T entity) {
		Assert.notNull(entity, "Entity must not be null");

		if (!entity.isDeleted()) {
			entity.setDeletedBy(null);
			entity.setDeletedOn(null);
		} else {
			if (entity.isDeleted() && entity.getDeletedBy() == null) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Assert.notNull(authentication, "Forbidden :: User is not Authenticated");
				
				CurrentUser<?> currentUser = (CurrentUser<?>) authentication.getPrincipal();
				Assert.notNull(currentUser, "Current User is null can not set deleted by");
				
				entity.setDeletedBy(currentUser.getUser().getId());
				entity.setDeletedOn(new Date());
			}
		}
	}
}
