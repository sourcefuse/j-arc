package com.sourcefuse.jarc.services.authservice.models;

import java.util.UUID;

import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_tenants", schema = "main")
public class UserTenant extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String locale;
  private UserStatus status;
  private UUID userId;
  private UUID tenantId;
  private UUID roleId;
}
