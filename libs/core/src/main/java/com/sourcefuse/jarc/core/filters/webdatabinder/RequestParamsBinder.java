package com.sourcefuse.jarc.core.filters.webdatabinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * This class is used to convert request param strings into target objects.
 *
 * Author: Adil Shaikh
 * */
@Slf4j
public class RequestParamsBinder<T> extends PropertyEditorSupport {

  private ObjectMapper objectMapper;
  private Class<T> targetType;
  /**
   * Constructs a RequestParamsBinder object.
   * Params:
   * objectMapper –It is used to read and write JSON data
   * targetType - Target class type
   * */
  public RequestParamsBinder(ObjectMapper objectMapper, Class<T> targetType) {
    this.objectMapper = objectMapper;
    this.targetType = targetType;
  }

  public RequestParamsBinder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }
 /**
  * Sets the property value by parsing a given String. May raise java.lang.IllegalArgumentException
  * if either the String is badly formatted or if this kind of property can't be expressed as text.
  * Params:
  * text – The string to be parsed.
  * */
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
