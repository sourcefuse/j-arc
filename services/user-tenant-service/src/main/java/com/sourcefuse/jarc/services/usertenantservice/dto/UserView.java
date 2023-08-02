package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.enums.Gender;
import com.sourcefuse.jarc.core.enums.RoleKey;
import com.sourcefuse.jarc.core.enums.UserStatus;
import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotBlank(message = "firstName must not be Empty")
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "middle_name")
  private String middleName;

  @NotBlank(message = "username must not be Empty")
  @Column(name = "username", nullable = false)
  private String username;

  @NotBlank(message = "email must not be Empty")
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

  @NotBlank(message = "defaultTenantId must not be Empty")
  @Column(name = "default_tenant_id", nullable = false)
  private UUID defaultTenantId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  @NotNull
  @Column(name = "role_id", nullable = false)
  private UUID roleId;

  @NotBlank(message = "tenantName must not be Empty")
  @Column(name = "name", nullable = false)
  private String tenantName;

  @Column(name = "key")
  private String tenantKey;

  @Column(name = "rolename")
  private String roleName;

  @Column(name = "roletype") 
  @Enumerated(EnumType.STRING)
  private RoleKey roleType;

  @NotNull
  @Column(name = "user_tenant_id", nullable = false)
  private UUID userTenantId;

//  @Column(name = "expires_on")
//  private Date expiresOn;
}
