package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.Gender;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v_users", schema = "main")
public class UserView extends UserModifiableEntity implements Serializable {

  private static final long serialVersionUID = 1905122041950251219L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "middle_name")
  private String middleName;

  @NotBlank
  @Column(name = "username", nullable = false)
  private String username;

  @NotBlank
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

  @NotBlank
  @Column(name = "default_tenant_id", nullable = false)
  private String defaultTenantId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @NotBlank
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  @NotBlank
  @Column(name = "role_id", nullable = false)
  private UUID roleId;

  @NotBlank
  @Column(name = "name", nullable = false)
  private String tenantName;

  @Column(name = "key")
  private String tenantKey;

  @Column(name = "rolename")
  private String roleName;

  @Column(name = "roletype")
  private Integer roleType;

  @NotBlank
  @Column(name = "user_tenant_id", nullable = false)
  private UUID userTenantId;

  @Column(name = "expires_on")
  private Date expiresOn;
}
