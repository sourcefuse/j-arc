package com.sourcefuse.jarc.services.usertenantservice.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "v_user_group")
public class UserGroupView extends Group {

    @Column(name = "user_tenant_id")
    private String userTenantId;
}
