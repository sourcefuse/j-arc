package com.sourcefuse.jarc.services.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenRequest {

  private String code;
  private String clientId;
}
