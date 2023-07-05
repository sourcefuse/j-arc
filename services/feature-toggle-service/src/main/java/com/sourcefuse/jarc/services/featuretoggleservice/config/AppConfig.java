package com.sourcefuse.jarc.services.featuretoggleservice.config;

import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.SystemLevelActivationStrategy;
import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.TenantLevelActivationStrategy;
import com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies.UserTenantLevelActivationStrategy;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.activation.ActivationStrategyProvider;
import org.togglz.core.activation.DefaultActivationStrategyProvider;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.spi.ActivationStrategy;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

  private final DataSource dataSource;

  private List<ActivationStrategy> activationStrategies = List.of(
    new SystemLevelActivationStrategy(),
    new UserTenantLevelActivationStrategy(),
    new TenantLevelActivationStrategy()
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
