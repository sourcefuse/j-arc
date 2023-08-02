package com.sourcefuse.jarc.authlib.config;

import com.sourcefuse.jarc.authlib.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String SWAGGER_ROLE = "SWAGGER";

  @Value("${springdoc.swagger-ui.path:/swagger-ui}")
  String swaggerUiPath;

  @Value("${springdoc.api-docs.path:/v3/api-docs}")
  String swaggerDocsPath;

  @Value("${swagger.auth.username:#{null}}")
  String swaggerUsername;

  @Value("${swagger.auth.password:#{null}}")
  String swaggerPassword;

  private final JwtAuthenticationFilter authenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http.csrf().disable();
    http
      .authorizeHttpRequests()
      .requestMatchers(
        swaggerDocsPath + "/**",
        swaggerUiPath + "/**",
        "/swagger-ui/**"
      )
      .hasAnyRole(SWAGGER_ROLE)
      .and()
      .authenticationManager(authManager(http));
    http
      .authorizeHttpRequests()
      .anyRequest()
      .permitAll()
      .and()
      .httpBasic()
      .and()
      .addFilterBefore(
        authenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      );
    return http.build();
  }

  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
      http.getSharedObject(AuthenticationManagerBuilder.class);
    if (
      swaggerUsername != null &&
      !swaggerUsername.isBlank() &&
      swaggerPassword != null &&
      !swaggerPassword.isBlank()
    ) {
      authenticationManagerBuilder
        .inMemoryAuthentication()
        .withUser(swaggerUsername)
        .password(new BCryptPasswordEncoder().encode(swaggerPassword))
        .roles(SWAGGER_ROLE)
        .and()
        .passwordEncoder(new BCryptPasswordEncoder());
    }
    return authenticationManagerBuilder.build();
  }
}
