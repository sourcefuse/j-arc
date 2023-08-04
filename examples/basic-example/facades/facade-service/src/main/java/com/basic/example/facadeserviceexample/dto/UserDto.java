package com.basic.example.facadeserviceexample.dto;

import com.basic.example.facadeserviceexample.enums.UserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  @NotNull(message = "Role ID cannot be null")
  private UUID roleId;

  @NotNull(message = "Tenant ID cannot be null")
  private UUID tenantId;

  private Integer status;

  private String authProvider;

  private String authId;

  private UUID userTenantId;

  @Valid
  @NotNull(message = "User details cannot be null")
  private User userDetails;

  public UserDto(
    User existingUser,
    UUID roleId,
    UserStatus status,
    UUID tenantId,
    UUID userTenantId,
    String authProvider
  ) {
    this.userDetails = existingUser;
    this.roleId = roleId;
    this.status = status.ordinal();
    this.tenantId = tenantId;
    this.userTenantId = userTenantId;
    this.authProvider = authProvider;
  }
}
