package com.sourcefuse.jarc.services.usertenantservice.commons;

import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class SoftDeleteEntity {

  boolean deleted;
  Date deletedOn;
  UUID deletedBy;
}
