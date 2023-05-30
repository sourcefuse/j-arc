package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_permissions", schema = "main")
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
