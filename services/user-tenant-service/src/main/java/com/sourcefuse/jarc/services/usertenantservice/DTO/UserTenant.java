package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.BaseEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.Column;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tenants",schema = "main")
public class UserTenant extends BaseEntity implements Serializable {
    //doubt:
    // implements IUserPrefs
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "locale")
    private String locale;

    @Column(name = "status")
    @Enumerated
    private UserStatus status;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
   // private User userId;
    @Column(name = "user_id",nullable = false)
    private UUID userId;

   // @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
   //private Tenant tenantId;
    @Column(name = "tenant_id",nullable = false)
    private UUID tenantId;

    //ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    //private Role roleId;
    @Column(name = "role_id",nullable = false)
    private UUID roleId;


    @OneToMany(mappedBy = "userTenantId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLevelPermission> userLevelPermissions;

    @OneToMany(mappedBy = "userTenantId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroup> userGroups;

}


