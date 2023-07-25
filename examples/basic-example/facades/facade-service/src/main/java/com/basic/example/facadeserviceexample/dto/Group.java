package com.basic.example.facadeserviceexample.dto;

import com.basic.example.facadeserviceexample.enums.UserTenantGroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group extends UserModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column
  private String name;

  @Column
  private String description;

  @Column(name = "photo_url")
  private String photoUrl;

  @Column(name = "group_type")
  @Enumerated(EnumType.STRING)
  @Transient
  private UserTenantGroupType groupType = UserTenantGroupType.TENANT;

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  @OneToMany(mappedBy = "group")
  private List<UserGroup> userGroups;

  @NotNull(message = "Tenant ID cannot be null")
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  public Group(UUID groupId) {
    this.id = groupId;
  }
}
