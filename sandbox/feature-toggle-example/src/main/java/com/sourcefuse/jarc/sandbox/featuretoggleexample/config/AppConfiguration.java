package com.sourcefuse.jarc.sandbox.featuretoggleexample.config;

import com.sourcefuse.jarc.sandbox.featuretoggleexample.enums.FeaturesEnum;
import com.sourcefuse.jarc.services.featuretoggleservice.services.ConvertEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.togglz.core.Feature;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.spi.FeatureProvider;

@Configuration
public class AppConfiguration {

  @Bean
  public FeatureProvider featureProvider() {
    return new EnumBasedFeatureProvider(FeaturesEnum.class);
  }

  @Component
  public class ConvertEnumImpl implements ConvertEnum {

    @Override
    public Feature getEnum(String enumName) throws IllegalArgumentException {
      try {
        return FeaturesEnum.valueOf(enumName);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid Enum name passed");
      }
    }
  }
}
