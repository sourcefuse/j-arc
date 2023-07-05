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

public class TenantLevelActivationStrategy implements ActivationStrategy {

  private static final StrategyEnums ID = StrategyEnums.TENANT;
  private static final StrategyEnums NAME = StrategyEnums.TENANT;
  private static final String TENANT_PARAMETER =
    StrategyEnums.TENANT.toString();

  @Override
  public String getId() {
    return ID.toString();
  }

  @Override
  public String getName() {
    return NAME.toString();
  }

  @Override
  public boolean isActive(FeatureState featureState, FeatureUser user) {
    String tenantIds = featureState.getParameter(TENANT_PARAMETER);

    if (Strings.isNotBlank(tenantIds)) {
      List<String> tenantList = Strings.splitAndTrim(tenantIds, ",");

      CurrentUser currUser = (CurrentUser) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

      if (
        currUser != null &&
        Strings.isNotBlank(currUser.getTenantId().toString())
      ) {
        String currUserTenantId = currUser.getTenantId().toString();
        return tenantList.contains(currUserTenantId);
      }
    }
    return false;
  }

  @Override
  public Parameter[] getParameters() {
    return new Parameter[] {
      ParameterBuilder
        .create(TENANT_PARAMETER)
        .label("Tenants")
        .largeText()
        .description("List of tenants for which the fetaure is active")
    };
  }
}
