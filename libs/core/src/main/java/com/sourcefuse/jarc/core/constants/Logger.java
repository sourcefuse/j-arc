package com.sourcefuse.jarc.core.constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {

  public static void error(Exception e) {
    log.error("{}: {} {}", e.getMessage(), e.getCause(), e.getStackTrace());
  }
}
