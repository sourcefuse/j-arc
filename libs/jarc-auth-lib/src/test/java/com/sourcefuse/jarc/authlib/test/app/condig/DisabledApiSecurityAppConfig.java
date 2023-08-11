package com.sourcefuse.jarc.authlib.test.app.condig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.sourcefuse.jarc.authlib.api.security.config.ApiSecurityConfig;
import com.sourcefuse.jarc.authlib.test.api.security.config.DisabledApiSecurityConfig;

@TestConfiguration
public class DisabledApiSecurityAppConfig {

	@Bean
	@Primary
	public ApiSecurityConfig apiSecurityConfig() {
		return new DisabledApiSecurityConfig();
	}

}