package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sourcefuse.jarc.core.enums.Gender;
import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", schema = "main")
public class User extends UserModifiableEntity {

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

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "default_tenant_id")
  //doubt:
  //this not found at Tenant class
  private Tenant defaultTenant;

  @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
  private UserCredentials credentials;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserTenant> userTenants;

  public User(UUID userId) {
    this.id = userId;
  }

  @JsonProperty("defaultTenantId")
  @NotNull(message = "DefaultTenant ID cannot be null")
  public UUID getTnt() {
    if (defaultTenant != null) {
      return this.defaultTenant.getId();
    }
    return null;
  }

  @JsonProperty("defaultTenantId")
  public void setTnt(UUID defaultTenantId) {
    this.defaultTenant = new Tenant(defaultTenantId);
  }
}
