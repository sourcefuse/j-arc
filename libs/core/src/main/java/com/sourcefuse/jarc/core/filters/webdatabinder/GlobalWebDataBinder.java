package com.sourcefuse.jarc.core.filters.webdatabinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.filters.models.Filter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
/**
 *  The class is used to intercept every request with request param containing
 *   Filter class type
 *   and convert them from strings into Filter type objects.
 *
 *    @InitBinder
 *    Annotation that identifies methods that initialize the WebDataBinder
 *    which will be used for populating command and form object
 *    arguments of annotated handler methods.
 *    Author: Adil Shaikh
 */
@ControllerAdvice
public class GlobalWebDataBinder {

  @InitBinder
  public <T> void initBinder(WebDataBinder binder) {
    Class<T> targetType = (Class<T>) Filter.class;
    binder.registerCustomEditor(
      targetType,
      new RequestParamsBinder<T>(new ObjectMapper(), targetType)
    );
  }
}
