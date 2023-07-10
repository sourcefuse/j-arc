package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.TenantConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class TenantConfigSpecification {

  private TenantConfigSpecification() {}

  public static Specification<TenantConfig> byTenantId(UUID tenantId) {
    return (
        Root<TenantConfig> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("tenant").get("id"), tenantId);
  }
}
