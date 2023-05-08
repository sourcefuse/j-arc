package com.sourcefuse.jarc.services.authservice.models;

import com.sourcefuse.jarc.services.authservice.enums.UserStatus;
import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
