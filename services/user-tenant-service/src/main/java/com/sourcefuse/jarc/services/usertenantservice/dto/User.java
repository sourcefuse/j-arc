package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
//@NoArgsConstructor
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", schema = "main")
//@SecondaryTable(name = "users")
public class User extends UserModifiableEntity implements Serializable {

  private static final long serialVersionUID = 1905122041950251212L;

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
  @Column(nullable = false)
  private String username;

  @NotBlank
  @Column(nullable = false)
  private String email;

  private String designation;

  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  private String phone;

  @Column(name = "auth_client_ids")
  private String authClientIds;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_login")
  private Date lastLogin;

  @Column(name = "photo_url")
  private String photoUrl;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Temporal(TemporalType.DATE)
  private Date dob;

  @Column(name = "default_tenant_id")
  private UUID defaultTenantId;

  @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
  private UserCredentials credentials;

  @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
  private List<UserTenant> userTenants;
}
