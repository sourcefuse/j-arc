package com.sourcefuse.jarc.core.models.base;

import com.sourcefuse.jarc.core.entitylisteners.SoftDeleteEntityListner;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(SoftDeleteEntityListner.class)
public abstract class SoftDeleteEntity extends BaseEntity {

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  Date deletedOn;

  UUID deletedBy;
}
