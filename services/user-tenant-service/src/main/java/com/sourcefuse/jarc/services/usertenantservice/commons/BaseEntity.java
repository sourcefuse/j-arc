package com.sourcefuse.jarc.services.usertenantservice.commons;

import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity extends SoftDeleteEntity {

  Date createdOn;
  Date modifiedOn;
}
