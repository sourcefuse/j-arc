package com.sourcefuse.jarc.services.featuretoggleservice.config;

import java.util.List;

import org.springframework.stereotype.Component;
import org.togglz.core.activation.ActivationStrategyProvider;
import org.togglz.core.spi.ActivationStrategy;

@Component
public class CustomActivationStrategyProvider implements ActivationStrategyProvider {

	@Override
	public List<ActivationStrategy> getActivationStrategies() {
		// TODO Auto-generated method stub
		return null;
	}

}
