package com.sourcefuse.jarc.services.authservice.models;

import java.util.UUID;
import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;
import jakarta.persistence.*;
import lombok.*;

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
