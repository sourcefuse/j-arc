package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_credentials")
public class UserCredentials extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1905122041950251212L;

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