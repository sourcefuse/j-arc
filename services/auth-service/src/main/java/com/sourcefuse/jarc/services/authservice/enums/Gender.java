package com.sourcefuse.jarc.services.authservice.enums;

public enum Gender {
  MALE("M"),
  FEMALE("F"),
  OTHER("O"),
  UNKNOWN("U");

  public final String label;

  Gender(String label) {
    this.label = label;
  }
}
