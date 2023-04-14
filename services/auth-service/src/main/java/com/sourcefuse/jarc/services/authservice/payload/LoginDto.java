package com.sourcefuse.jarc.services.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

  private String username;
  private String password;
  private String clientId;
  private String clientSecret;
}
