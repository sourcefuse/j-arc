package com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies;

import org.togglz.core.activation.Parameter;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;

// This activation strategy does not use any parameters 
//it straight decides if it is enabled or not 
//does not depend on any parameters
public class SystemLevelActivationStrategy implements ActivationStrategy {

	private static final String ID = "system-level";
	private static final String NAME = "System level";
	// private static final String START_TIME_PARAMETER = "start";
	// private static final String END_TIME_PARAMETER = "end";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public boolean isActive(FeatureState featureState, FeatureUser user) {
		// TODO Auto-generated method stub
		System.out.println("in the custom activation strategy class");
		return featureState.isEnabled();
		// return false;
	}

	@Override
	public Parameter[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
