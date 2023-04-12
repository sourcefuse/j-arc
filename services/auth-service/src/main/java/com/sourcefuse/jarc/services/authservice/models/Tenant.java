package com.sourcefuse.jarc.services.authservice.models;

import java.util.UUID;

import com.sourcefuse.jarc.services.authservice.enums.TenantStatus;
import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tenants", schema = "main")
public class Tenant extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  private TenantStatus status;
  private String key;
  private String website;
  private String address;
  private String city;
  private String state;
  private String zip;
  private String country;
}
