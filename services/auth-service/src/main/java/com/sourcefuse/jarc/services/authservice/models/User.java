package com.sourcefuse.jarc.services.authservice.models;

import com.sourcefuse.jarc.services.authservice.enums.Gender;
import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
  private Gender gender;
  private Date dob;
  private UUID defaultTenantId;
  private List<Integer> authClientIds;
}
