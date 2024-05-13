package com.sourcefuse.jarc.services.authservice.config;

import com.sourcefuse.jarc.authlib.cors.CorsFilter;
import com.sourcefuse.jarc.authlib.security.JwtAuthenticationFilter;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.handlers.OAuth2AuthenticationFailureHandler;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.handlers.OAuth2AuthenticationSuccessHandler;
import com.sourcefuse.jarc.services.authservice.oauth2.auth.request.resolver.CustomOAuth2AuthorizationRequestResolver;
import com.sourcefuse.jarc.services.authservice.oauth2.request.filters.OAuth2AuthorizationRequestFilter;
import com.sourcefuse.jarc.services.authservice.oauth2.services.CustomOAuth2UserService;
import com.sourcefuse.jarc.services.authservice.oauth2.services.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@ConditionalOnProperty(
  value = "auth.security-config.enable",
  havingValue = "true"
)
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

  private final CorsFilter corsFilter;

  private final CustomOAuth2UserService customOAuth2UserService;

  private final CustomOidcUserService customOidc2UserService;

  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  private final ClientRegistrationRepository clientRegistrationRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http.csrf().disable();
    http
      .oauth2Login()
      .authorizationEndpoint()
      .authorizationRequestResolver(
        new CustomOAuth2AuthorizationRequestResolver(
          clientRegistrationRepository
        )
      )
      .and()
      .userInfoEndpoint()
      .userService(customOAuth2UserService)
      .oidcUserService(customOidc2UserService)
      .and()
      .successHandler(oAuth2AuthenticationSuccessHandler)
      .failureHandler(oAuth2AuthenticationFailureHandler)
      .permitAll();
    http.addFilterBefore(
      new OAuth2AuthorizationRequestFilter(),
      OAuth2AuthorizationRequestRedirectFilter.class
    );

    http.authorizeHttpRequests().anyRequest().permitAll();

    http
      .addFilterBefore(
        authenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .addFilterBefore(corsFilter, ChannelProcessingFilter.class);

    return http.build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain swaggerFilterChain(HttpSecurity http)
    throws Exception {
    http
      .securityMatchers(e ->
        e.requestMatchers(
          swaggerDocsPath + "/**",
          swaggerUiPath + "/**",
          "/swagger-ui/**"
        )
      )
      .authorizeHttpRequests(authorize ->
        authorize.anyRequest().hasRole(SWAGGER_ROLE)
      )
      .httpBasic();
    return http.build();
  }

  @Autowired
  public void swaggerConfigureInMemoryAuthUsers(
    AuthenticationManagerBuilder auth
  ) throws Exception {
    // Get the existing authentication manager configuration and extend it

    if (
      swaggerUsername != null &&
      !swaggerUsername.isBlank() &&
      swaggerPassword != null &&
      !swaggerPassword.isBlank()
    ) {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      UserDetails user = User
        .builder()
        .username(swaggerUsername)
        .password(passwordEncoder.encode(swaggerPassword))
        .roles(SWAGGER_ROLE)
        .build();
      auth
        .inMemoryAuthentication()
        .passwordEncoder(passwordEncoder)
        .withUser(user);
    }
  }
}
