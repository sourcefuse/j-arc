package com.basic.example.facadeserviceexample.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserGroup extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  private Group group;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_tenant_id", nullable = false)
  private UserTenant userTenant;

  @JsonProperty("isOwner")
  @Column(name = "is_owner")
  private boolean isOwner;

  @NotNull(message = "Tenant ID cannot be null")
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  @JsonProperty("groupId")
  @NotNull(message = "Group ID cannot be null")
  public UUID getGrp() {
    if (group != null) {
      return this.group.getId();
    }
    return null;
  }

  @JsonProperty("groupId")
  public void setGrp(UUID groupId) {
    this.group = new Group(groupId);
  }

  @JsonProperty("userTenantId")
  @NotNull(message = "UserTenant ID cannot be null")
  public UUID getUsrTnt() {
    if (userTenant != null) {
      return this.userTenant.getId();
    }
    return null;
  }

  @JsonProperty("userTenantId")
  public void setUsrTnt(UUID userTenantId) {
    this.userTenant = new UserTenant(userTenantId);
  }
}
