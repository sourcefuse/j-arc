package com.sourcefuse.userintentservice.DTO;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sourcefuse.userintentservice.commons.ConfigKey;
import com.sourcefuse.userintentservice.commons.UserModifiableEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenant_configs",schema = "main")
public class TenantConfig extends UserModifiableEntity {

    @Id
    private String id;

    @NotBlank
    @Column(name = "config_key", nullable = false)
    private ConfigKey configKey;

    //doubt:
//    @Column(name = "config_value")
//    private Object configValue;

    @ManyToOne
    @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
    private Tenant tenantId;


}
