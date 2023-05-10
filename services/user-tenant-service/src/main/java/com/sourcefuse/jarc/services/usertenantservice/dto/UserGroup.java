package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_groups", schema = "main")
public class UserGroup extends UserModifiableEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "group_id", nullable = false)
  private UUID groupId;

  @Column(name = "user_tenant_id", nullable = false)
  private UUID userTenantId;

  @Column(name = "is_owner")
  private boolean isOwner;
}
