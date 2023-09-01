package com.sourcefuse.jarc.core.filters.webdatabinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;

@Slf4j
public class RequestParamsBinder<T> extends PropertyEditorSupport {

  private ObjectMapper objectMapper;
  private Class<T> targetType;

  public RequestParamsBinder(ObjectMapper objectMapper, Class<T> targetType) {
    this.objectMapper = objectMapper;
    this.targetType = targetType;
  }

  public RequestParamsBinder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    if (StringUtils.isEmpty(text)) {
      setValue(null);
    } else {
      T obj;
      try {
        log.info("Request params received in request" + text);
        obj = objectMapper.readValue(text, targetType);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }
      setValue(obj);
    }
  }
}
