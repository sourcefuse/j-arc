package com.sourcefuse.jarc.services.authservice.payload;

import java.util.UUID;

import com.sourcefuse.jarc.services.authservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.authservice.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

  private User user;
  private UUID defaultTenantId;
  private UUID roleId;
  private String authId;
  private AuthProvider authProvider;
}
