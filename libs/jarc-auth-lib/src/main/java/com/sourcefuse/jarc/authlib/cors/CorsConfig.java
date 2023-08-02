package com.sourcefuse.jarc.authlib.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

  @Value("${cors.url:#{null}}")
  String corsUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    if (corsUrl != null) {
      registry
        .addMapping("/api/**")
        .allowedOrigins(corsUrl)
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true);
    }
  }
}
