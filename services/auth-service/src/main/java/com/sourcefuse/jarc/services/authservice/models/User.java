package com.sourcefuse.jarc.services.authservice.models;

import com.sourcefuse.jarc.core.enums.Gender;
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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", schema = "main")
public class User extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String firstName;
  private String lastName;
  private String middleName;
  private String username;
  private String email;
  private String designation;
  private String phone;
  private Timestamp lastLogin;
  @Enumerated(EnumType.STRING)
  private Gender gender;
  private LocalDate dob;
  private UUID defaultTenantId;
  private List<UUID> authClientIds;
}
