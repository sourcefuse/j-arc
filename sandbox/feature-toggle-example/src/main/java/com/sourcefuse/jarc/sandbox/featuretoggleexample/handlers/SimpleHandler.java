package com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleHandler implements FeatureHandlers {

  @Override
  public boolean handle() {
    log.info("The Handler was called and it returns true");
    return true;
  }
}
