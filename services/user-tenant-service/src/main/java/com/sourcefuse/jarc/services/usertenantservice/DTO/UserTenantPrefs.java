package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserConfigKey;
import jakarta.persistence.*;
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
@Table(name = "user_tenant_prefs", schema = "main")
public class UserTenantPrefs extends UserModifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // @Enumerated(EnumType.STRING)
    @Column(name = "config_key", nullable = false)

    private UserConfigKey configKey;

    // @Column(name = "config_value", nullable = true,columnDefinition = "jsonb")
    //private Object configValue;
    //  @Type(JsonType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config_value", columnDefinition = "jsonb")
    private Object configValue;

    /*@ManyToOne(optional = true)
    @JoinColumn(name = "user_tenant_id")
    private UserTenant userTenantId; doubt :: */
    @Column(name = "user_tenant_id")
    private UUID userTenantId;

}
