package com.sourcefuse.jarc.services.usertenantservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupCheckDto {

  @Id
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private boolean isSignedUp;
}
