package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_tenant_prefs", schema = "main")
public class UserTenantPrefs extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @Column(name = "config_key", nullable = false)
  private String configKey;

  @NotNull
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config_value", columnDefinition = "jsonb")
  private Object configValue;

  @JsonIgnore
  @ManyToOne(optional = true)
  @JoinColumn(name = "user_tenant_id")
  private UserTenant userTenant;

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
