package com.sourcefuse.jarc.services.authservice.models;

import java.util.UUID;

import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;

import jakarta.persistence.Entity;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_permissions", schema = "main")
public class UserLevelPermission extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private UUID userTenantId;
  private String permission;
  private boolean allowed;
}
