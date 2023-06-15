package com.sourcefuse.jarc.services.featuretoggleservice.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.activation.ActivationStrategyProvider;
import org.togglz.core.activation.DefaultActivationStrategyProvider;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.spi.FeatureProvider;

import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.SystemLevelActivationStrategy;
import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.TenantLevelActivationStrategy;
import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.UserTenantLevelActivationStrategy;
import com.sourcefuse.jarc.services.featuretoggleservice.enums.Features;

@Configuration
public class AppConfig {

	@Autowired
	private DataSource dataSource;
	private ActivationStrategy activationStrategy = new SystemLevelActivationStrategy();

	private List<ActivationStrategy> activationStrategies = List.of(new SystemLevelActivationStrategy(),
			new UserTenantLevelActivationStrategy(), new TenantLevelActivationStrategy());

	@Bean
	public JDBCStateRepository stateRepository() {
		return new JDBCStateRepository(dataSource, "feature_toggle");
	}

	@Bean
	public FeatureProvider featureProvider() {
		return new EnumBasedFeatureProvider(Features.class);
	}

	@Bean
	public ActivationStrategyProvider activationStrategyProvider() {
		DefaultActivationStrategyProvider activationStrategyProvider = new DefaultActivationStrategyProvider();
		// activationStrategyProvider.addActivationStrategy(activationStrategy);
		activationStrategyProvider.addActivationStrategies(activationStrategies);
		return activationStrategyProvider;
	}

}
