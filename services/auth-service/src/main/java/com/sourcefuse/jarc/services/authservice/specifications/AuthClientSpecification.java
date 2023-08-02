package com.sourcefuse.jarc.services.authservice.specifications;

import com.sourcefuse.jarc.services.authservice.models.AuthClient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class AuthClientSpecification {

  private AuthClientSpecification() {}

  public static Specification<AuthClient> byClientId(String clientId) {
    return (
      Root<AuthClient> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> builder.equal(root.get("clientId"), clientId);
  }

  public static Specification<AuthClient> byClientSecret(String clientSecret) {
    return (
      Root<AuthClient> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> builder.equal(root.get("clientSecret"), clientSecret);
  }
  public static Specification<AuthClient> byAllowedClients(
    List<String> allowedClients
  ) {
    return (
      Root<AuthClient> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> builder.in(root.get("clientId")).value(allowedClients);
  }

  public static Specification<AuthClient> byClientIdAndClientSecret(
    String clientId,
    String clientSecret
  ) {
    return Specification
      .where(AuthClientSpecification.byClientId(clientId))
      .and(AuthClientSpecification.byClientSecret(clientSecret));
  }

  
}
