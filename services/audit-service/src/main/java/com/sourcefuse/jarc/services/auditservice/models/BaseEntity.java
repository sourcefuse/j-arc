package com.sourcefuse.jarc.services.auditservice.models;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity extends SoftDeleteEntity {

    Date createdOn;
    Date modifiedOn;

}