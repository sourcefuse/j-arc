package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.AuthClient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class AuthClientsSpecification {

  private AuthClientsSpecification() {}

  public static Specification<AuthClient> byClientIdIn(
    List<String> clientIdList
  ) {
    return (
        Root<AuthClient> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.in(root.get("clientId")).value(clientIdList);
  }
}
