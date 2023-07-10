package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserTenantSpecification {

  private UserTenantSpecification() {}

  public static Specification<UserTenant> byRoleId(UUID roleId) {
    return (
        Root<UserTenant> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("role").get("id"), roleId);
  }

  public static Specification<UserTenant> byUserId(UUID userId) {
    return (
        Root<UserTenant> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("user").get("id"), userId);
  }

  public static Specification<UserTenant> byTenantId(UUID tenantId) {
    return (
        Root<UserTenant> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("tenant").get("id"), tenantId);
  }

  public static Specification<UserTenant> orderByIdAsc() {
    return (
        Root<UserTenant> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      (Predicate) query.orderBy(builder.asc(root.get("id")));
  }

  public static Specification<UserTenant> byUserIdAndTenantIdOrderByIdAsc(
    UUID userId,
    UUID tenantId
  ) {
    return Specification
      .where(UserTenantSpecification.byUserId(userId))
      .and(UserTenantSpecification.byTenantId(tenantId));
  }

  public static Specification<UserTenant> byUserIdAndTenantId(
    UUID userId,
    UUID tenantId
  ) {
    return Specification
      .where(UserTenantSpecification.byUserId(userId))
      .and(UserTenantSpecification.byTenantId(tenantId));
  }
}
