package com.sourcefuse.usertenantservice.DTO;

import jakarta.persistence.*;

import com.sourcefuse.usertenantservice.commons.ConfigKey;
import com.sourcefuse.usertenantservice.commons.UserModifiableEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
