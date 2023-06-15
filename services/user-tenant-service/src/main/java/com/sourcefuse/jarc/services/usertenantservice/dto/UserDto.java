package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.enums.UserStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private UUID roleId;

  private UUID tenantId;

  private Integer status;

  private String authProvider;

  private String authId;

  private UUID userTenantId;

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
