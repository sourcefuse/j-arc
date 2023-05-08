package com.sourcefuse.jarc.services.authservice.models;

import com.sourcefuse.jarc.services.authservice.models.base.UserModifiableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_clients", schema = "main")
public class AuthClient extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String clientId;
  private String clientSecret;
  private String redirectUrl;
  private String secret;
  private long accessTokenExpiration;
  private long refreshTokenExpiration;
  private long authCodeExpiration;
}
