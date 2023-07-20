package com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.featuretoggleservice.enums.StrategyEnums;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.togglz.core.activation.Parameter;
import org.togglz.core.activation.ParameterBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.util.Strings;

public class FeatureToggleStrategy implements ActivationStrategy {

  private static final String strategy = "FeatureToggle";
  private static final String ID = strategy;
  private static final String NAME = strategy;
  private static final String TENANT_PARAMETER = StrategyEnums.TENANT.toString();
  private static final String USER_TENANT_PARAMETER = StrategyEnums.USER_TENANT.toString();

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean isActive(FeatureState featureState, FeatureUser user) {
    boolean allowed = true;
    CurrentUser currUser = (CurrentUser) SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getPrincipal();

    String tenantIds = featureState.getParameter(TENANT_PARAMETER);
    if (Strings.isNotBlank(tenantIds)) {
      List<String> tenantList = Strings.splitAndTrim(tenantIds, ",");

      if (
        currUser != null &&
        Strings.isNotBlank(currUser.getTenantId().toString())
      ) {
        String currUserTenantId = currUser.getTenantId().toString();
        allowed = allowed && tenantList.contains(currUserTenantId);
      }
    }

    String userTenantIds = featureState.getParameter(USER_TENANT_PARAMETER);

    if (Strings.isNotBlank(userTenantIds)) {
      List<String> userTenantList = Strings.splitAndTrim(userTenantIds, ",");

      if (
        currUser != null &&
        Strings.isNotBlank(currUser.getUserTenantId().toString())
      ) {
        String currUserUTenantId = currUser.getUserTenantId().toString();

        allowed = allowed && userTenantList.contains(currUserUTenantId);
      }
    }
    return allowed;
  }

  @Override
  public Parameter[] getParameters() {
    return new Parameter[] {
      ParameterBuilder
        .create(TENANT_PARAMETER)
        .label(TENANT_PARAMETER)
        .largeText()
        .description("List of tenants for which the fetaure is active"),
      ParameterBuilder
        .create(USER_TENANT_PARAMETER)
        .label(USER_TENANT_PARAMETER)
        .largeText()
        .description("List of user tenant for which the fetaure is active")
    };
  }
}
