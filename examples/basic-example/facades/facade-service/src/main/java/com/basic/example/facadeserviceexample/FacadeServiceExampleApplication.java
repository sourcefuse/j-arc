package com.basic.example.facadeserviceexample;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@SecurityScheme(
  name = "bearerAuth",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
@ComponentScan(
  basePackages = {
    "com.basic.example.facadeserviceexample", "com.sourcefuse.jarc.authlib",
  }
)
public class FacadeServiceExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(FacadeServiceExampleApplication.class, args);
  }

  @Bean
  public WebClient initWebClient() {
    // Create a WebClient instance
    return WebClient.builder().build();
  }
}
