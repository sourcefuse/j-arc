package com.sourcefuse.jarc.sandbox.featuretoggleexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.sourcefuse.jarc.core","com.sourcefuse.jarc.services.authservice","com.sourcefuse.jarc.services.featuretoggleservice","com.sourcefuse.jarc.sandbox.featuretoggleexample"})
public class FeatureToggleExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeatureToggleExampleApplication.class, args);
	}

}
