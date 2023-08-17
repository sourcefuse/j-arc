package com.sourcefuse.jarc.services.featuretoggleservice.config;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.activation.ActivationStrategyProvider;
import org.togglz.core.activation.DefaultActivationStrategyProvider;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.spi.ActivationStrategy;
import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.FeatureToggleStrategy;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

  private final DataSource dataSource;

  private List<ActivationStrategy> activationStrategies = List.of(
    new FeatureToggleStrategy()
  );

  @Bean
  public JDBCStateRepository stateRepository() {
    return new JDBCStateRepository(dataSource, "feature_toggle");
  }

  @Bean
  public ActivationStrategyProvider activationStrategyProvider() {
    DefaultActivationStrategyProvider activationStrategyProvider = 
    new DefaultActivationStrategyProvider();
    activationStrategyProvider.addActivationStrategies(activationStrategies);
    return activationStrategyProvider;
  }
}
