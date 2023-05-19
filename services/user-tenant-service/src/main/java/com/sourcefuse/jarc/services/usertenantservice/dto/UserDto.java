package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  @NotBlank
  @Column(nullable = false)
  private UUID roleId;

  @NotBlank
  @Column(nullable = false)
  private UUID tenantId;

  private Integer status;

  @Column(name = "auth_provider")
  private String authProvider;

  @Column(name = "auth_id")
  private String authId;

  @Column(name = "user_tenant_id")
  private UUID userTenantId;

  @OneToOne(cascade = CascadeType.ALL)
  private User userDetails;

  public UserDto(
    User existingUser,
    UUID roleId,
    UserStatus status,
    UUID tenantId,
    UUID userTenantId,
    String authProvider
  ) {}
}
