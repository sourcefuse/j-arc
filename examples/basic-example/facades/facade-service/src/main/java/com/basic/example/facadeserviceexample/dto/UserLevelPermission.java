package com.basic.example.facadeserviceexample.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelPermission extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank(message = "permission must not be Empty")
  @Column(nullable = false)
  private String permission;

  @NotNull
  @Column(nullable = false)
  private boolean allowed = true;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_tenant_id")
  private UserTenant userTenantId;
}
