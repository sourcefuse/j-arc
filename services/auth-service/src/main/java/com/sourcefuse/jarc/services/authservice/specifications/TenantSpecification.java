package com.sourcefuse.jarc.services.authservice.specifications;

import com.sourcefuse.jarc.services.authservice.models.Tenant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public final class TenantSpecification {

  private TenantSpecification() {}

  public static Specification<Tenant> byKey(String key) {
    return (
      Root<Tenant> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> {
      return builder.equal(root.get("key"), key);
    };
  }
}
