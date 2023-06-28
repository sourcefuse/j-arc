package com.sourcefuse.jarc.sandbox.featuretoggleexample.handlers;

import com.sourcefuse.jarc.services.featuretoggleservice.annotation.FeatureHandlers;
import org.springframework.stereotype.Component;

@Component("HelloHandler")
public class HelloHandler implements FeatureHandlers {

  @Override
  public boolean handle() {
    System.out.println("inside the hello handler");
    return true;
  }
}
