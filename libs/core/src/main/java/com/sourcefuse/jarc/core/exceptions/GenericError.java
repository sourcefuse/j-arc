package com.sourcefuse.jarc.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericError {

  private String status;
  private String errCode;
  private String message;
}
