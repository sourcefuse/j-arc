package com.basic.example.facadeserviceexample.dto;

import com.basic.example.facadeserviceexample.enums.RoleKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Role extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank(message = "Name must not be Empty")
  @Column(nullable = false)
  private String name;

  @NotNull(message = "roleType must not be null")
  @Column(nullable = false, name = "role_type")
  @Enumerated(EnumType.STRING)
  private RoleKey roleType;

  @Column(name = "permissions")
  private List<String> permissions;

  @Column(name = "allowed_clients")
  private List<String> allowedClients;

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  @OneToMany(mappedBy = "role")
  private List<UserTenant> userTenants;

  @OneToOne
  @JoinColumn(
    name = "createdBy",
    referencedColumnName = "id",
    insertable = false,
    updatable = false
  )
  private UserView createdByUser;

  @OneToOne
  @JoinColumn(
    name = "modifiedBy",
    referencedColumnName = "id",
    insertable = false,
    updatable = false
  )
  private UserView modifiedByUser;

  public Role(UUID roleId) {
    this.id = roleId;
  }
}
