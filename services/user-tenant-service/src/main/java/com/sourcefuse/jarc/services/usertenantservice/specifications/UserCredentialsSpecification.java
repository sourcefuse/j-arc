package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserCredentials;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserCredentialsSpecification {

  private UserCredentialsSpecification() {}

  public static Specification<UserCredentials> byUserId(UUID userId) {
    return (
        Root<UserCredentials> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("userId").get("id"), userId);
  }
}
