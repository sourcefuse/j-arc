package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class RoleSpecification {

  private RoleSpecification() {}

  public static Specification<Role> byId(UUID id) {
    return (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
      builder.equal(root.get("id"), id);
  }
}