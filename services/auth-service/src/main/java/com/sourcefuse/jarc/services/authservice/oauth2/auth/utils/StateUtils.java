package com.sourcefuse.jarc.services.authservice.oauth2.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StateUtils {

  public static final String clientIdParamKey = "clientId";

  private StateUtils() {}

  public static String encode(String clientId) throws JsonProcessingException {
    Map<String, String> dataMap = new HashMap<>();
    dataMap.put(clientIdParamKey, clientId);
    dataMap.put("id", UUID.randomUUID().toString());

    // Convert the map to a JSON string
    String jsonString = new Gson().toJson(dataMap);
    return Base64
      .getEncoder()
      .encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
  }

  public static String decode(String encodedState) {
    byte[] decodedBytes = Base64
      .getUrlDecoder()
      .decode(encodedState.getBytes(StandardCharsets.UTF_8));
    String decodedString = new String(decodedBytes);

    @SuppressWarnings("unchecked")
    // Convert the JSON string to a Map
    Map<String, Object> dataMap = new Gson().fromJson(decodedString, Map.class);

    // Retrieve and return the state from the map
    return (String) dataMap.get(clientIdParamKey);
  }
}
