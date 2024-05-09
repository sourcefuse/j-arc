package com.sourcefuse.jarc.services.usertenantservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupCheckDto {

  @Id
  @Column(nullable = false)
  @NotNull
  private Long id;

  @Column(nullable = false)
  private boolean isSignedUp;
}
