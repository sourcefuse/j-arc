package com.sourcefuse.jarc.services.authservice.config;

import com.sourcefuse.jarc.services.authservice.security.JwtAuthenticationEntryPoint;
import com.sourcefuse.jarc.services.authservice.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

  private JwtAuthenticationEntryPoint authenticationEntryPoint;
  private final JwtAuthenticationFilter authenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    // http
    // .authorizeHttpRequests(authorize -> authorize
    // .requestMatchers("/auth/**", "/keycloak/**", "/ping")
    // .permitAll()
    // .anyRequest()
    // .authenticated())
    // .exceptionHandling(exception ->
    // exception.authenticationEntryPoint(authenticationEntryPoint));
    http.cors().and().csrf().disable();

    http.addFilterBefore(
      authenticationFilter,
      UsernamePasswordAuthenticationFilter.class
    );
    return http.build();
  }
}
