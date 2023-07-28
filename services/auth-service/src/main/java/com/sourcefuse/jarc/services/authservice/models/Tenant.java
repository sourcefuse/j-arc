package com.sourcefuse.jarc.services.authservice.models;

import com.sourcefuse.jarc.core.enums.TenantStatus;
import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tenants", schema = "main")
public class Tenant extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  @Enumerated(EnumType.STRING)
  private TenantStatus status;
  private String key;
  private String website;
  private String address;
  private String city;
  private String state;
  private String zip;
  private String country;
}
