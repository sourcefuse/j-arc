package com.sourcefuse.jarc.services.featuretoggleservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.activation.ActivationStrategyProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.spi.FeatureProvider;

@Configuration
@RequiredArgsConstructor
public class TogglzConfig {

  private final JDBCStateRepository stateRepository;

  private final ActivationStrategyProvider activationStrategyProvider;

  private final FeatureProvider featureProvider;

  @Bean
  public FeatureManager featureManager(
    StateRepository stateRepository,
    FeatureProvider featureProvider
  ) {
    return new FeatureManagerBuilder()
      .stateRepository(stateRepository)
      .featureProvider(featureProvider)
      .activationStrategyProvider(activationStrategyProvider)
      .build();
  }
}