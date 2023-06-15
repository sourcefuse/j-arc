package com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies;

import org.togglz.core.activation.Parameter;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;

import com.sourcefuse.jarc.services.featuretoggleservice.enums.StrategyEnums;

// This activation strategy does not use any parameters 
//it straight decides if it is enabled or not 
//does not depend on any parameters
public class SystemLevelActivationStrategy implements ActivationStrategy {

	private static final StrategyEnums ID = StrategyEnums.System;
	private static final StrategyEnums NAME = StrategyEnums.System;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return ID.toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME.toString();
	}

	@Override
	public boolean isActive(FeatureState featureState, FeatureUser user) {
		// TODO Auto-generated method stub
		return featureState.isEnabled();
		// return false;
	}

	@Override
	public Parameter[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
