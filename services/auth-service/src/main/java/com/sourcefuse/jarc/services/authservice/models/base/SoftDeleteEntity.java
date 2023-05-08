package com.sourcefuse.jarc.services.authservice.models.base;

import jakarta.persistence.Column;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoftDeleteEntity {

  @Column(columnDefinition = "boolean default false", nullable = true)
  private Boolean deleted = false;

  private Date deletedOn;
  private String deletedBy;
}
