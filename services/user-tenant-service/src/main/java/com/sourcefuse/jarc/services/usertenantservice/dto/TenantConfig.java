package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.ConfigKey;
import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class TenantConfig extends UserModifiableEntity {

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
