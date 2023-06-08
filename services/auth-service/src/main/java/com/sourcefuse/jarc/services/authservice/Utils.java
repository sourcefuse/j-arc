package com.sourcefuse.jarc.services.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public final class Utils {
  private Utils() {}

  public static ObjectMapper getObjectMapperInstance() {
    return new Jackson2ObjectMapperBuilder()
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .modulesToInstall(new JavaTimeModule())
      .build();
  }
}
