package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_credentials")
public class UserCredentials extends BaseEntity {

  @Id
  private String id;

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
