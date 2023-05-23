package com.sourcefuse.jarc.services.authservice.specifications;

import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.services.authservice.models.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public final class RoleSpecification {

  private RoleSpecification() {}

  public static Specification<Role> byName(String name) {
    return (
      Root<Role> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> {
      return builder.equal(root.get("name"), name);
    };
  }

  public static Specification<Role> byRoleType(RoleKey roleType) {
    return (
      Root<Role> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> {
      return builder.equal(root.get("roleType"), roleType);
    };
  }
}
