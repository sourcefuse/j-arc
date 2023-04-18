package com.sourcefuse.jarc.services.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private String clientId;
    private String clientSecret;
}
