package com.sourcefuse.usertenantservice.DTO;

import com.sourcefuse.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_groups",schema = "main")
public class UserGroup extends UserModifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group groupId;

    @ManyToOne
    @JoinColumn(name = "user_tenant_id", nullable = false)
    private UserTenant userTenantId;

    @Column(name = "is_owner", nullable = false)
    private boolean isOwner = false;

}