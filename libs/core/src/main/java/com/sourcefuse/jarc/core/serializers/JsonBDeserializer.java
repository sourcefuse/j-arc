package com.sourcefuse.jarc.core.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class JsonBDeserializer extends StdDeserializer<Object> {

  private static final long serialVersionUID = 8558133505323154005L;

  public JsonBDeserializer() {
    super(Object.class);
  }

  @Override
  public Object deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = p.getCodec().readTree(p);
    return mapper.writeValueAsString(node);
  }
}
