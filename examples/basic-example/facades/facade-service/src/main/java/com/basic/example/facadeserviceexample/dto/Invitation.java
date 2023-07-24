package com.basic.example.facadeserviceexample.dto;

import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation extends SoftDeleteEntity {

    private UUID id;

    private String email;

    private LocalDateTime expires;
}
