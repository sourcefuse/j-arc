package com.sourcefuse.jarc.services.authservice.specifications;

import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserTenantSpecification {

  private UserTenantSpecification() {}

  public static Specification<UserTenant> byUserId(UUID userId) {
    return (
      Root<UserTenant> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> {
      return builder.equal(root.get("userId"), userId);
    };
  }

  public static Specification<UserTenant> byTenantId(UUID tenantId) {
    return (
      Root<UserTenant> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> {
      return builder.equal(root.get("tenantId"), tenantId);
    };
  }

  public static Specification<UserTenant> byNotInStatuses(
    List<UserStatus> statuses
  ) {
    return (
      Root<UserTenant> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> {
      return builder.not(root.get("status").in(statuses));
    };
  }

  public static Specification<UserTenant> byUserIdAndTenantId(
    UUID userId,
    UUID tenantId
  ) {
    return Specification
      .where(UserTenantSpecification.byUserId(userId))
      .and(UserTenantSpecification.byTenantId(tenantId));
  }

  public static Specification<UserTenant> byUserIdTenantIdAndStatusNotIn(
    UUID userId,
    UUID tenantId,
    List<UserStatus> statuses
  ) {
    return Specification
      .where(UserTenantSpecification.byUserIdAndTenantId(userId, tenantId))
      .and(UserTenantSpecification.byNotInStatuses(statuses));
  }
}
