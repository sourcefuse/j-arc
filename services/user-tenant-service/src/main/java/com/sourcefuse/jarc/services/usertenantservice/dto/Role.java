package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema = "main")
public class Role extends UserModifiableEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, name = "role_type")
  //doubt::
  private RoleType roleType;

  //@ElementCollection
  @Column(name = "permissions")
  private List<String> permissions;

  //doubt:
  // @ElementCollection(name = "allowed_clients")
  //@ElementCollection
  @Column(name = "allowed_clients")
  private List<String> allowedClients;

  @OneToMany(mappedBy = "roleId")
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
}
