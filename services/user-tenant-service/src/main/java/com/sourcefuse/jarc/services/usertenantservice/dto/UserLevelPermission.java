package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.*;
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

  //doubt:
  //implements UserPermission<String>

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String permission;

  @Column(nullable = false)
  private boolean allowed = true;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_tenant_id")
  private UserTenant userTenantId;
}
