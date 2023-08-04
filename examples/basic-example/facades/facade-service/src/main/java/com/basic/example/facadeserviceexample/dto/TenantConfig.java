package com.basic.example.facadeserviceexample.dto;

import com.basic.example.facadeserviceexample.enums.ConfigKey;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantConfig extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @Column(name = "config_key", nullable = false)
  private ConfigKey configKey;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config_value", columnDefinition = "jsonb")
  private String configValue;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
  private Tenant tenant;

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
}
