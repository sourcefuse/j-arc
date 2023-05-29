package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.core.models.base.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema = "main")
@EqualsAndHashCode
public class Role extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank(message = "Name must not be null")
  @Column(nullable = false)
  private String name;

  @NotNull(message = "roleType must not be null")
  @Column(nullable = false, name = "role_type")
  //doubt::
  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  //@ElementCollection
  @Column(name = "permissions")
  private List<String> permissions;

  //doubt:
  // @ElementCollection(name = "allowed_clients")
  //@ElementCollection
  @Column(name = "allowed_clients")
  private List<String> allowedClients;

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
