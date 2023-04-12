package com.sourcefuse.jarc.services.authservice.models.base;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity extends SoftDeleteEntity {

  Date createdOn;
  Date modifiedOn;
}
