package com.sourcefuse.jarc.sandbox.featuretoggleexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({FeatureToggleExampleApplication.FEATURE_EXAMPLE, FeatureToggleExampleApplication.CORE_PACKAGE,FeatureToggleExampleApplication.AUTH_PACKAGE,FeatureToggleExampleApplication.FEATURE_PACKAGE})
public class FeatureToggleExampleApplication {

	  public static final String CORE_PACKAGE = "com.sourcefuse.jarc.core";
	  public static final String AUTH_PACKAGE = "com.sourcefuse.jarc.services.authservice";
	  public static final String FEATURE_PACKAGE = "com.sourcefuse.jarc.services.featuretoggleservice";
	  public static final String FEATURE_EXAMPLE = "com.sourcefuse.jarc.sandbox.featuretoggleexample";

	public static void main(String[] args) {
		SpringApplication.run(FeatureToggleExampleApplication.class, args);
	}

}
