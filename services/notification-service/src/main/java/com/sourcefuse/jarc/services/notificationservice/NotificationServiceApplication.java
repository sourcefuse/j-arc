package com.sourcefuse.jarc.services.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@ComponentScan(
  {
    "com.sourcefuse.jarc.core",
    "com.sourcefuse.jarc.services.notificationservice"
  }
)
public class NotificationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApplication.class, args);
  }

  /**
   * TO_DO: need to remove this code when authentication service is integrated,
   * This code is added to allow access to all url in this app for temporary
   * purpose
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(CsrfConfigurer::disable)
      .authorizeHttpRequests(requests ->
        requests.requestMatchers("/**").permitAll()
      )
      .build();
  }
}
