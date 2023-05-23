package com.sourcefuse.jarc.services.auditservice.test.models;

import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Role extends SoftDeleteEntity {

  private UUID id;
  private String name;
  private String permissions;
}
