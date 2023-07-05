package com.sourcefuse.jarc.core.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class JsonBSerializer extends JsonSerializer<Object> {

  @Override
  public void serialize(
    Object value,
    JsonGenerator gen,
    SerializerProvider serializers
  ) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Object jsonObject = value;
    if (value instanceof String) {
      jsonObject = objectMapper.readValue(value.toString(), Object.class);
    }
    objectMapper.writeValue(gen, jsonObject);
  }
}
