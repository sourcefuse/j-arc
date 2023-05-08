package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.ConfigKey;
import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

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

    // doubt:
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config_value", columnDefinition = "jsonb")
    private String configValue;


    //    @ManyToOne
//    @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
//    private Tenant tenantId;
    @Column(name = "tenant_id")
    private UUID tenantId;


}
