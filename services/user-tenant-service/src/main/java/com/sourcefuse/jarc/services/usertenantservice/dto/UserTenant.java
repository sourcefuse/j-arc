package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_tenants", schema = "main")
@Slf4j
public class UserTenant extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "locale")
  private String locale;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
  private Tenant tenant;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  private Role role;

  @OneToMany(
    mappedBy = "userTenantId",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<UserLevelPermission> userLevelPermissions;

  @OneToMany(
    mappedBy = "userTenant",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<UserGroup> userGroups;

  public UserTenant(UUID userTenantId) {
    this.id = userTenantId;
  }

  @JsonProperty("userId")
  @NotNull(message = "User ID cannot be null")
  public UUID getUsr() {
    if (user != null) {
      return this.user.getId();
    }
    return null;
  }

  @JsonProperty("userId")
  public void setUsr(UUID userId) {
    this.user = new User(userId);
  }

  @JsonProperty("tenantId")
  @NotNull(message = "Tenant ID cannot be null")
  public UUID getTnt() {
    if (tenant != null) {
      return this.tenant.getId();
    }
    return null;
  }

  @JsonProperty("tenantId")
  public void setTnt(UUID tenantId) {
    this.tenant = new Tenant(tenantId);
  }

  @JsonProperty("roleId")
  @NotNull(message = "Role ID cannot be null")
  public UUID getRol() {
    if (role != null) {
      return this.role.getId();
    }
    return null;
  }

  @JsonProperty("roleId")
  public void setRol(UUID roleId) {
    this.role = new Role(roleId);
  }
}
