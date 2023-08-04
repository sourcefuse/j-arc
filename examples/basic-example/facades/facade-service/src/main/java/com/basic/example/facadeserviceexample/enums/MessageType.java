package com.basic.example.facadeserviceexample.enums;

public enum MessageType {
  PUSH("Push"),
  EMAIL("Email"),
  SMS("SMS");

  private final String name;

  MessageType(String value) {
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
