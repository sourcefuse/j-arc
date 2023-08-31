package com.sourcefuse.jarc.services.usertenantservice.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.filters.models.Filter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

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
