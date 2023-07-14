package com.sourcefuse.jarc.sandbox.featuretoggleexample.enums;

import org.togglz.core.Feature;

public enum FeaturesEnum implements Feature {
  HELLO("HELLO"),
  HI("HI"),
  HEY("HEY");

  private final String name;

  FeaturesEnum(String value) {
    this.name = value;
  }

  public String value() {
    return this.name;
  }

  @Override
  public String toString() {
    return name;
  }
}
