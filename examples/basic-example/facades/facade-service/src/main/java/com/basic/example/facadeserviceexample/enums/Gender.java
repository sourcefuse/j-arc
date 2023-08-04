package com.basic.example.facadeserviceexample.enums;

public enum Gender {
  MALE("M"),
  FEMALE("F"),
  OTHER("O"),
  UNKNOWN("U");

  public final String label;

  Gender(String label) {
    this.label = label;
  }

  public String getValue() {
    return label;
  }
}
