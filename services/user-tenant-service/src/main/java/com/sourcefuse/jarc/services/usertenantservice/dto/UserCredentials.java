package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredentials extends UserModifiableEntity {

  @Id
  private UUID id;

  @NotBlank
  @Column(name = "auth_provider", nullable = false)
  private String authProvider;

  @Column(name = "auth_id")
  private String authId;

  @Column(name = "auth_token")
  private String authToken;

  @Column(name = "secret_key")
  private String secretKey;

  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User userId;
}
