
package com.sourcefuse.jarc.core.models.base;

import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UserModifiableEntity extends SoftDeleteEntity {

  @CreatedBy
  UUID createdBy;

  @LastModifiedBy
  UUID modifiedBy;

  @CreatedDate
  LocalDateTime createdOn;

  @LastModifiedDate
  LocalDateTime modifiedOn;
}
