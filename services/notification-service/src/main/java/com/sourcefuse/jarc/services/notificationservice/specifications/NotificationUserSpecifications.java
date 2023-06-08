package com.sourcefuse.jarc.services.notificationservice.specifications;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;

public final class NotificationUserSpecifications {

	private NotificationUserSpecifications() {}

	public static Specification<NotificationUser> byNotificationId(UUID notificationId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("notification").get("id"),
				notificationId);
	}
}
