
package com.sourcefuse.jarc.services.auditservice.models;

import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sourcefuse.jarc.services.auditservice.audit.entitylistener.SoftDeleteEntityListner;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, SoftDeleteEntityListner.class})
public abstract class UserModifiableEntity extends BaseEntity {

	@CreatedBy
	UUID createdBy;

	@LastModifiedBy
	UUID modifiedBy;
}