package com.sourcefuse.jarc.sandbox.featuretoggleexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.togglz.core.Feature;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.spi.FeatureProvider;

import com.sourcefuse.jarc.sandbox.featuretoggleexample.enums.FeaturesEnum;
import com.sourcefuse.jarc.services.featuretoggleservice.services.ConvertEnum;


@Configuration
public class AppConfiguration {

	@Bean
	public FeatureProvider featureProvider() {
		return new EnumBasedFeatureProvider(FeaturesEnum.class);
	}
	
	//@Bean
	@Component
	public class ConvertEnumImpl implements ConvertEnum {

		@Override
		public Feature getEnum(String enumName) throws IllegalArgumentException{
			// TODO Auto-generated method stub
			try {
			return FeaturesEnum.valueOf(enumName);
			} catch(IllegalArgumentException e) {
				throw new RuntimeException("Invalid Enum name passed");
			}
		}
		
	}
}
