package com.sourcefuse.jarc.services.auditservice.models;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class SoftDeleteEntity {

    boolean deleted;
    Date deletedOn;
    UUID deletedBy;

}