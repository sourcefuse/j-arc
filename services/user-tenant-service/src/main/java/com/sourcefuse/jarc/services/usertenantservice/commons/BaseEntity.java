package com.sourcefuse.jarc.services.usertenantservice.commons;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity extends SoftDeleteEntity {

  LocalDateTime createdOn;
  LocalDateTime modifiedOn;
}
