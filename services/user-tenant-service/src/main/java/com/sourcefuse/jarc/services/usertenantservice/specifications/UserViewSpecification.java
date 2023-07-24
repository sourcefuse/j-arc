package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserViewSpecification {

  private UserViewSpecification() {}

  public static Specification<UserView> byUserTenantId(UUID userTenantId) {
    return (
        Root<UserView> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("userTenantId"), userTenantId);
  }

  public static Specification<UserView> byTenantId(UUID tenantId) {
    return (
        Root<UserView> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("tenantId"), tenantId);
  }

  public static Specification<UserView> byRoleType(Integer roleType) {
    return (
        Root<UserView> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.notEqual(root.get("roleType"), roleType);
  }

  public static Specification<UserView> byId(UUID id) {
    return (
        Root<UserView> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("id"), id);
  }

  public static Specification<UserView> byUserIdAndTenantId(
    UUID id,
    UUID tenantId
  ) {
    return Specification
      .where(UserViewSpecification.byId(id))
      .and(UserViewSpecification.byTenantId(tenantId));
  }

  public static Specification<UserView> byTenantIdAndNotRoleType(
    UUID tenantId,
    Integer roleType
  ) {
    return Specification
      .where(UserViewSpecification.byTenantId(tenantId))
      .and(UserViewSpecification.byRoleType(roleType));
  }
}
