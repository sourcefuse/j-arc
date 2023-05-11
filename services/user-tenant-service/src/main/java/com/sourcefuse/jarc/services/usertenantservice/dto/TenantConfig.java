package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.ConfigKey;
import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "tenant_configs", schema = "main")
public class TenantConfig extends UserModifiableEntity implements Serializable {

  private static final long serialVersionUID = 1905122041950251211L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(name = "config_key", nullable = false)
  private ConfigKey configKey;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config_value", columnDefinition = "jsonb")
  private String configValue;

  @Column(name = "tenant_id")
  private UUID tenantId;
}
