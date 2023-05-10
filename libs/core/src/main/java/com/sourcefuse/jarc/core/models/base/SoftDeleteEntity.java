package com.sourcefuse.jarc.core.models.base;

import com.sourcefuse.jarc.core.entitylisteners.SoftDeleteEntityListner;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(SoftDeleteEntityListner.class)
public abstract class SoftDeleteEntity implements BaseEntity {

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    LocalDateTime deletedOn;

    UUID deletedBy;
}
