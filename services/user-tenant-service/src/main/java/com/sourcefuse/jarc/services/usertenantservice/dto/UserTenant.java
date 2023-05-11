package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.BaseEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_tenants", schema = "main")
public class UserTenant extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1905122041950251216L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "locale")
  private String locale;

  @Column(name = "status")
  @Enumerated
  private UserStatus status;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  @Column(name = "role_id", nullable = false)
  private UUID roleId;

  @OneToMany(
    mappedBy = "userTenantId",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<UserLevelPermission> userLevelPermissions;

  @OneToMany(
    mappedBy = "userTenantId",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<UserGroup> userGroups;
}
