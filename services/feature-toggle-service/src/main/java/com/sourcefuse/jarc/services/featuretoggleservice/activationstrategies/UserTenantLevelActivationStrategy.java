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

//Strategy that will check if the particular userTenant has access to the provided feature

public class UserTenantLevelActivationStrategy implements ActivationStrategy {

  private static final StrategyEnums ID = StrategyEnums.USER_TENANT;
  private static final StrategyEnums NAME = StrategyEnums.USER_TENANT;
  private static final String USER_TENANT_PARAMETER = StrategyEnums.USER_TENANT.toString();

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
    String userTenantIds = featureState.getParameter(USER_TENANT_PARAMETER);

    if (Strings.isNotBlank(userTenantIds)) {
      List<String> userTenantList = Strings.splitAndTrim(userTenantIds, ",");

      CurrentUser currUser = (CurrentUser) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

      if (
        currUser != null &&
        Strings.isNotBlank(currUser.getUserTenantId().toString())
      ) {
        String currUserUTenantId = currUser.getUserTenantId().toString();

        return userTenantList.contains(currUserUTenantId);
      }
    }
    return false;
  }

  @Override
  public Parameter[] getParameters() {
    return new Parameter[] {
      ParameterBuilder
        .create(USER_TENANT_PARAMETER)
        .label("UserTenants")
        .largeText()
        .description("List of user tenant for which the fetaure is active")
    };
  }
}
