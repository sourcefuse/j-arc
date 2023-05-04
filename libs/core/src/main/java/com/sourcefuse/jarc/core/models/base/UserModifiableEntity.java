package com.sourcefuse.jarc.core.models.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
  Date createdOn;

  @LastModifiedDate
  Date modifiedOn;
}
