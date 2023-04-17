package com.sourcefuse.usertenantservice.DTO;

import com.sourcefuse.usertenantservice.commons.UserModifiableEntity;
import com.sourcefuse.usertenantservice.enums.UserTenantGroupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups", schema = "main")
public class Group extends UserModifiableEntity {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "group_type")
    @Enumerated(EnumType.STRING)
    private UserTenantGroupType groupType = UserTenantGroupType.Tenant;

    @OneToMany(mappedBy = "groupId")
    private List<UserGroup> userGroups;

}



