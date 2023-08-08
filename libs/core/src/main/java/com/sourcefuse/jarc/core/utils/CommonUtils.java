package com.sourcefuse.jarc.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sourcefuse.jarc.core.dtos.ErrorDetails;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public final class CommonUtils {

  private static ObjectMapper objectMapper = new ObjectMapper()
    .registerModule(new JavaTimeModule());

  private CommonUtils() {}

  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();
    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  public static String getErrorInString(String requestUri, String errorMessage)
    throws JsonProcessingException {
    ErrorDetails errorDetails = new ErrorDetails(
      LocalDateTime.now(),
      errorMessage,
      "url=" + requestUri
    );
    return objectMapper.writeValueAsString(errorDetails);
  }
}
