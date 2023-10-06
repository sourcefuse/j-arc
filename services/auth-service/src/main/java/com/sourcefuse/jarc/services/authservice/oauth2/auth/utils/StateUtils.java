package com.sourcefuse.jarc.services.authservice.oauth2.auth.utils;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StateUtils {

  public static final String CLIENT_ID_PARAM_KEY = "clientId";
  private static final Gson gson = new Gson();

  private StateUtils() {}

  public static String encode(String clientId) {
    Map<String, String> dataMap = new HashMap<>();
    dataMap.put(CLIENT_ID_PARAM_KEY, clientId);
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
    String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

    @SuppressWarnings("unchecked")
    // Convert the JSON string to a Map
    Map<String, Object> dataMap = gson.fromJson(decodedString, Map.class);

    // Retrieve and return the state from the map
    return (String) dataMap.get(CLIENT_ID_PARAM_KEY);
  }
}
