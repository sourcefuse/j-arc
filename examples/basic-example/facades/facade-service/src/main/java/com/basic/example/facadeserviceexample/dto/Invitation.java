package com.basic.example.facadeserviceexample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation extends SoftDeleteEntity {

    private UUID id;

    private String email;

    private LocalDateTime expires;
}
