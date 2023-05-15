package com.sourcefuse.jarc.services.usertenantservice.dto;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserTenantGroupType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups", schema = "main")
@Inheritance(strategy = InheritanceType.JOINED)
public class Group extends UserModifiableEntity implements Serializable {

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

  @OneToMany(mappedBy = "group")
  private List<UserGroup> userGroups;

  public Group(UUID groupId) {
    this.id = groupId;
  }
}
