package com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies;

import com.sourcefuse.jarc.services.featuretoggleservice.enums.StrategyEnums;
import org.togglz.core.activation.Parameter;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;

// This activation strategy does not use any parameters
//it straight decides if it is enabled or not
//does not depend on any parameters
public class SystemLevelActivationStrategy implements ActivationStrategy {

  private static final StrategyEnums ID = StrategyEnums.SYSTEM;
  private static final StrategyEnums NAME = StrategyEnums.SYSTEM;

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
    return featureState.isEnabled();
  }

  @Override
  public Parameter[] getParameters() {
    return new Parameter[0];
  }
}
