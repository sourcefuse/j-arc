package com.sourcefuse.jarc.services.featuretoggleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;

@SpringBootApplication()
// @EnableAutoConfiguration
@ComponentScan({ "com.sourcefuse.jarc.services.featuretoggleservice",
		"com.sourcefuse.jarc.core", "com.sourcefuse.jarc.services.authservice" })
// @EnableJpaRepositories
// @EnableJpaRepositories(basePackages = {
// "com.sourcefuse.jarc.services.featuretoggleservice.repositories" })
// @EnableJpaRepositories(basePackages = {
// "com.sourcefuse.jarc.core.repositories" })
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)

public class FeatureToggleServiceApplication {

	/**
	 * TODO: need to remove this code when authentication service is integrated This
	 * code is added to allow access to all url in this app for temporary purpose
	 */
	// @Bean
	// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	// return http.csrf(CsrfConfigurer::disable)
	// .authorizeHttpRequests(requests ->
	// requests.requestMatchers("/**").permitAll()).build();
	// }

	public static void main(String[] args) {

		SpringApplication.run(FeatureToggleServiceApplication.class, args);

	}

}
