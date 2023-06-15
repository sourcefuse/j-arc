package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {

  private UserSpecification() {}

  public static Specification<User> byEmail(String email) {
    return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
      builder.equal(root.get("email"), email);
  }

  public static Specification<User> byUsername(String username) {
    return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
      builder.equal(root.get("username"), username);
  }

  public static Specification<User> byIdNot(UUID id) {
    return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
      builder.notEqual(root.get("id"), id);
  }

  public static Specification<User> byUserNameOrEmail(
    String userName,
    String email
  ) {
    return Specification
      .where(UserSpecification.byUsername(userName))
      .or(UserSpecification.byEmail(email));
  }

  public static Specification<User> byIdNotAndUsername(
    UUID id,
    String userName
  ) {
    return Specification
      .where(UserSpecification.byIdNot(id))
      .and(UserSpecification.byUsername(userName));
  }
}
