package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.Gender;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v_users", schema = "main")
public class UserView extends UserModifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "designation")
    private String designation;

    @Column(name = "phone")
    private String phone;

    @Column(name = "auth_client_ids")
    private String authClientIds;

    @Column(name = "last_login")
    private String lastLogin;

    @Column(name = "photo_url")
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "default_tenant_id", nullable = false)
    private String defaultTenantId;

    @Column(name = "status")
    private UserStatus status;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "name", nullable = false)
    private String tenantName;

    @Column(name = "key")
    private String tenantKey;

    @Column(name = "rolename")
    private String roleName;

    @Column(name = "roletype")
    private Integer roleType;

    @Column(name = "user_tenant_id", nullable = false)
    private UUID userTenantId;

    @Column(name = "expires_on")
    private Date expiresOn;


}
