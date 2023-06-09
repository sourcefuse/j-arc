package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserGroup;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserGroupsSpecification {

  private UserGroupsSpecification() {}

  public static Specification<UserGroup> byGroupId(UUID groupId) {
    return (
        Root<UserGroup> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("group"), groupId);
  }

  public static Specification<UserGroup> byUserTenantId(UUID userTenantId) {
    return (
        Root<UserGroup> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("userTenant"), userTenantId);
  }

  public static Specification<UserGroup> byIsOwner(boolean isOwner) {
    return (
        Root<UserGroup> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("isOwner"), isOwner);
  }

  public static Specification<UserGroup> byId(UUID id) {
    return (
        Root<UserGroup> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("id"), id);
  }

  public static Specification<UserGroup> byGroupIdAndUserTenantId(
    UUID groupId,
    UUID userTenantId
  ) {
    return Specification
      .where(UserGroupsSpecification.byGroupId(groupId))
      .and(UserGroupsSpecification.byUserTenantId(userTenantId));
  }

  public static Specification<UserGroup> byGroupIdAndIsOwner(
    UUID groupId,
    boolean isOwner
  ) {
    return Specification
      .where(UserGroupsSpecification.byGroupId(groupId))
      .and(UserGroupsSpecification.byIsOwner(isOwner));
  }

  public static Specification<UserGroup> byGroupIdAndIdAndIsOwner(
    UUID groupId,
    UUID userGrpId,
    boolean isOwner
  ) {
    return Specification
      .where(UserGroupsSpecification.byGroupId(groupId))
      .and(UserGroupsSpecification.byId(userGrpId))
      .and(UserGroupsSpecification.byIsOwner(isOwner));
  }

  public static Specification<UserGroup> byGroupIdOrIdOrUserTenantId(
    UUID groupId,
    UUID userGrpId,
    UUID userTenantId
  ) {
    return Specification
      .where(UserGroupsSpecification.byGroupId(groupId))
      .or(UserGroupsSpecification.byId(userGrpId))
      .or(UserGroupsSpecification.byUserTenantId(userTenantId));
  }
}
