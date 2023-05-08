package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_clients", schema = "main")
public class AuthClient extends BaseEntity {

    //  implements IAuthClient doubt::
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(nullable = false)
    private String secret;

    @Column(name = "access_token_expiration", nullable = false)
    private Long accessTokenExpiration;

    @Column(name = "refresh_token_expiration", nullable = false)
    private Long refreshTokenExpiration;

    @Column(name = "auth_code_expiration", nullable = false)
    private Long authCodeExpiration;

    /**
     * for stubbing test case commnet on PROD::
     */
    public AuthClient(UUID id) {
        this.id = id;
    }

}
