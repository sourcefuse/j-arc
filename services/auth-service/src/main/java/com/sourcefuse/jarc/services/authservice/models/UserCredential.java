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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_credentials", schema = "main")
public class UserCredential extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String authProvider;
  private String authId;
  private String authToken;
  private String password;
  private UUID userId;
}
