package com.sourcefuse.jarc.services.usertenantservice.DTO;

import jakarta.persistence.*;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserConfigKey;
import lombok.*;

import java.util.UUID;
@Data
@Entity(name = "user_tenant_prefs")
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_tenant_prefs",schema = "main")
public class UserTenantPrefs extends UserModifiableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "config_key", nullable = false)
    private UserConfigKey configKey;
    //doubt:
//    @Column(name = "config_value", nullable = false)
//    private Object configValue;

   // @Column(name = "config_value", nullable = true,columnDefinition = "jsonb")
    //private Object configValue;
  //  @Type(JsonType.class)
    @Column(columnDefinition = "JSON",name = "config_value")
    private String configValue;

    /*@ManyToOne(optional = true)
    @JoinColumn(name = "user_tenant_id")
    private UserTenant userTenantId; doubt :: */
    @Column(name = "user_tenant_id")
    private UUID userTenantId;

}
