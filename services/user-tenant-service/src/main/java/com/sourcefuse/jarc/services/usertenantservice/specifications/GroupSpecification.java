package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class GroupSpecification {

  private GroupSpecification() {}

  public static Specification<Group> byTenantId(UUID tenantId) {
    return (
        Root<Group> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("tenantId"), tenantId);
  }

  public static Specification<Group> byGroupId(UUID groupId) {
    return (
        Root<Group> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("id"), groupId);
  }

  public static Specification<Group> byGroupIdAndTenantId(
    UUID groupId,
    UUID tenantId
  ) {
    return Specification
      .where(GroupSpecification.byGroupId(groupId))
      .and(GroupSpecification.byTenantId(tenantId));
  }
}
