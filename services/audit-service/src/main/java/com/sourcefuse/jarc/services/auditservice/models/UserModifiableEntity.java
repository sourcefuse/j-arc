 
package com.sourcefuse.jarc.services.auditservice.models;

import java.util.UUID;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class UserModifiableEntity extends  BaseEntity {

    UUID createdBy;
    UUID modifiedBy;
}