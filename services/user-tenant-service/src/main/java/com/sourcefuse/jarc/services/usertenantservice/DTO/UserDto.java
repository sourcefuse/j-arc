package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

//@Entity

/*** This model need not to persisted*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * @Id
     * @GeneratedValue(strategy = GenerationType.UUID)
     * private UUID id;
     */

    @Column(nullable = false)
    private UUID roleId;

    @Column(nullable = false)
    private UUID tenantId;

    private Integer status;

    @Column(name = "auth_provider")
    private String authProvider;

    @Column(name = "auth_id")
    private String authId;

    @Column(name = "user_tenant_id")
    private UUID userTenantId;

    @OneToOne(cascade = CascadeType.ALL)
    private User userDetails;

    public UserDto(User existingUser, UUID roleId, UserStatus status, UUID tenantId, UUID userTenantId, String authProvider) {
    }
}
