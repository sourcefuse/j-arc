package com.sourcefuse.jarc.services.authservice.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.sourcefuse.jarc.services.authservice.models.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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

  public static Specification<User> byUserNameOrEmail(
    String username,
    String email
  ) {
    return Specification
      .where(UserSpecification.byUsername(username))
      .or(UserSpecification.byEmail(email));
  }
}
