package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserConfigKey;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class UserTenantPrefsSpecification {

  private UserTenantPrefsSpecification() {}

  public static Specification<UserTenantPrefs> byUserTenantId(
    UUID userTenantId
  ) {
    return (
        Root<UserTenantPrefs> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("userTenant"), userTenantId);
  }

  public static Specification<UserTenantPrefs> byConfigKey(
    UserConfigKey configKey
  ) {
    return (
        Root<UserTenantPrefs> root,
        CriteriaQuery<?> query,
        CriteriaBuilder builder
      ) ->
      builder.equal(root.get("configKey"), configKey);
  }

  public static Specification<UserTenantPrefs> byUserTenantIdAndConfigKey(
    UUID userTenantId,
    UserConfigKey configKey
  ) {
    return Specification
      .where(UserTenantPrefsSpecification.byUserTenantId(userTenantId))
      .and(UserTenantPrefsSpecification.byConfigKey(configKey));
  }
}
