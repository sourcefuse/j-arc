package com.sourcefuse.jarc.services.usertenantservice.commons;

import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class UserModifiableEntity extends BaseEntity {

  UUID createdBy;
  UUID modifiedBy;
}
