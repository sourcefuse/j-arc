package com.sourcefuse.jarc.services.usertenantservice.DTO;

import com.sourcefuse.jarc.services.usertenantservice.commons.UserModifiableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

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

    //@ManyToOne
    //@JoinColumn(name = "group_id", nullable = false)
    //UserGroup.groupId' targets an unknown entity named 'java.lang.String' doubt::
    // private Group groupId;
    @Column(name = "group_id", nullable = false)
    private UUID groupId;

    // @ManyToOne
    //@JoinColumn(name = "user_tenant_id", nullable = false)
    //  private UserTenant userTenantId;
    @Column(name = "user_tenant_id", nullable = false)
    private UUID userTenantId;

    // @Transient
    @Column(name = "is_owner")
    private boolean isOwner = false;

}