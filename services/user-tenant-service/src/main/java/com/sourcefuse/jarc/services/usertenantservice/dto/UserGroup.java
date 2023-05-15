package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
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
@Entity
@Builder
@Table(name = "user_groups", schema = "main")
@EqualsAndHashCode(callSuper = false)
public class UserGroup extends UserModifiableEntity implements Serializable {

  private static final long serialVersionUID = 1905122041950251213L;

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

  @Column(name = "is_owner")
  private boolean isOwner;

  @JsonProperty("groupId")
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
