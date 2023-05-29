package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_clients", schema = "main")
public class AuthClient extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(name = "client_id", nullable = false)
  private String clientId;

  @Column(name = "client_secret")
  private String clientSecret;

  @Column(name = "redirect_url")
  private String redirectUrl;

  @NotBlank
  @Column(nullable = false)
  private String secret;

  @NotNull
  @Column(name = "access_token_expiration", nullable = false)
  private Long accessTokenExpiration;

  @NotNull
  @Column(name = "refresh_token_expiration", nullable = false)
  private Long refreshTokenExpiration;

  @NotNull
  @Column(name = "auth_code_expiration", nullable = false)
  private Long authCodeExpiration;

  /**
   * for stubbing test case commnet on PROD::
   */
  public AuthClient(UUID id) {
    this.id = id;
  }
}
