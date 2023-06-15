package com.sourcefuse.jarc.services.notificationservice.specifications;

import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class NotificationUserSpecifications {

  private NotificationUserSpecifications() {}

  public static Specification<NotificationUser> byNotificationId(
    UUID notificationId
  ) {
    return (
        Root<NotificationUser> root,
        CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder
      ) ->
      criteriaBuilder.equal(root.get("notification").get("id"), notificationId);
  }

  public static Specification<NotificationUser> byId(UUID id) {
    return (
        Root<NotificationUser> root,
        CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder
      ) ->
      criteriaBuilder.equal(root.get("id"), id);
  }
}
