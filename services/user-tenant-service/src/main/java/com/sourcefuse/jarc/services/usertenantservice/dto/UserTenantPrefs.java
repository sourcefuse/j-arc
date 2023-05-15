package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserConfigKey;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tenant_prefs", schema = "main")
public class UserTenantPrefs
  extends UserModifiableEntity
  implements Serializable {

  private static final long serialVersionUID = 1905122041950251217L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  // @Enumerated(EnumType.STRING)
  @Column(name = "config_key", nullable = false)
  private UserConfigKey configKey;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config_value", columnDefinition = "jsonb")
  private Object configValue;

  @JsonIgnore
  @ManyToOne(optional = true)
  @JoinColumn(name = "user_tenant_id")
  private UserTenant userTenant;

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
