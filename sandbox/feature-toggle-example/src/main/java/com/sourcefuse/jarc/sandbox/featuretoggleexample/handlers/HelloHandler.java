package com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("HelloHandler")
public class HelloHandler implements FeatureHandlers {

  @Override
  public boolean handle() {
    log.info("This is inside a handler");
    return true;
  }
}
