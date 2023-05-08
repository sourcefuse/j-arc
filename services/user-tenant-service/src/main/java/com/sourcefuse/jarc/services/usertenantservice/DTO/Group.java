package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.jarc.services.usertenantservice.enums.UserTenantGroupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups", schema = "main")
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
    private UserTenantGroupType groupType = UserTenantGroupType.TENANT;

    @OneToMany(mappedBy = "groupId")
    private List<UserGroup> userGroups;

}



