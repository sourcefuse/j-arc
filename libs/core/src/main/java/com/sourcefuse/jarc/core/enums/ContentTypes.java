package com.sourcefuse.jarc.core.enums;

public enum ContentTypes {
  APPLICATION_JSON("application/json");

  private final String name;

  ContentTypes(String value) {
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
