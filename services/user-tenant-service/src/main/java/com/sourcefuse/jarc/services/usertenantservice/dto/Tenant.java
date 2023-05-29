package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.TenantStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants", schema = "main")
public class Tenant extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TenantStatus status;

  @Column(name = "KEY")
  private String key;

  private String website;

  private String address;

  private String city;

  private String state;

  private String zip;

  private String country;

  @Column(name = "primary_contact_email")
  private String primaryContactEmail;

  @Column(name = "allowed_domain")
  private String allowedDomain;

  @Column(name = "tenant_type")
  private String tenantType;

  @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
  private List<TenantConfig> tenantConfigs;

  @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
  private List<UserTenant> userTenants;

  public Tenant(UUID tenantId) {
    this.id = tenantId;
  }
}
