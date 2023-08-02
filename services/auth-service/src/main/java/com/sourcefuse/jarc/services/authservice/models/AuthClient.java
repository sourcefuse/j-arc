package com.sourcefuse.jarc.services.authservice.models;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "auth_clients", schema = "main")
public class AuthClient extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String clientId;
  private String clientSecret;
  private String redirectUrl;
  private String secret;
  private long accessTokenExpiration;
  private long refreshTokenExpiration;
  private long authCodeExpiration;
}
