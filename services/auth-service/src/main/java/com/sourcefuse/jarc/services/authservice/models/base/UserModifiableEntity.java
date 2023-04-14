package com.sourcefuse.jarc.services.authservice.models.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModifiableEntity
    extends BaseEntity {

  String createdBy;
  String modifiedBy;
}
