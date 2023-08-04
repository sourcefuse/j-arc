package com.basic.example.facadeserviceexample.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation extends SoftDeleteEntity {

  private UUID id;

  private String email;

  private LocalDateTime expires;
}
