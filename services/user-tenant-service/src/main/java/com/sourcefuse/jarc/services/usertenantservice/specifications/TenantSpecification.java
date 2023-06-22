package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class TenantSpecification {

  private TenantSpecification() {}

  public static Specification<Tenant> byKey(String key) {
    return (
        Root<Tenant> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("key"), key);
  }

  public static Specification<Tenant> byTenantId(UUID tenantId) {
    return (
        Root<Tenant> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("id"), tenantId);
  }
}
