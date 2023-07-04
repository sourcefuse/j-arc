package com.sourcefuse.jarc.services.usertenantservice.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

  private JsonUtils() {}

  public static String asJsonString(Object obj) throws Exception {
    return new ObjectMapper().writeValueAsString(obj);
  }
}
