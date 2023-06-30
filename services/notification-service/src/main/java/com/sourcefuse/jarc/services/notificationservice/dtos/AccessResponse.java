package com.sourcefuse.jarc.services.notificationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse {

  Integer ttl;
  String cipherKey;
}
