package com.sourcefuse.userintentservice.DTO;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  @Id
  @Column(nullable = true)
 Long id;

  @Column(nullable = false)
  private String roleId;

  @Column(nullable = false)
  private String tenantId;

  private Integer status;

  @Column(name = "auth_provider")
  private String authProvider;

  @Column(name = "auth_id")
  private String authId;

  @Column(name = "user_tenant_id")
  private String userTenantId;

  @OneToOne(cascade = CascadeType.ALL)
  private User userDetails;


}
