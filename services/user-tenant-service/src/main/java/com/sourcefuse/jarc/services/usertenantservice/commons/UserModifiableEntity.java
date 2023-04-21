package com.sourcefuse.jarc.services.usertenantservice.commons;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class UserModifiableEntity extends  BaseEntity{

    UUID createdBy;
    UUID modifiedBy;
}
