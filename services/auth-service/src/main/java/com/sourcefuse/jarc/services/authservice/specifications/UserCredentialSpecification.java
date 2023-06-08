package com.sourcefuse.jarc.services.authservice.specifications;

import com.sourcefuse.jarc.services.authservice.models.UserCredential;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserCredentialSpecification {

  private UserCredentialSpecification() {}

  public static Specification<UserCredential> byUserId(UUID userId) {
    return (
      Root<UserCredential> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> builder.equal(root.get("userId"), userId);
  }

  public static Specification<UserCredential> byAuthId(String authId) {
    return (
      Root<UserCredential> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> builder.equal(root.get("authId"), authId);
  }

  public static Specification<UserCredential> byAuthProvider(
    String authProvider
  ) {
    return (
      Root<UserCredential> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) -> builder.equal(root.get("authProvider"), authProvider);
  }

  public static Specification<UserCredential> byAuthIdAndAuthProvider(
    String authId,
    String authProvider
  ) {
    return Specification
      .where(UserCredentialSpecification.byAuthId(authId))
      .and(UserCredentialSpecification.byAuthProvider(authProvider));
  }
}
