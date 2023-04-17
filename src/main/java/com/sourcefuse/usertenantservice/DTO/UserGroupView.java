package com.sourcefuse.usertenantservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "v_user_group")
public class UserGroupView extends Group {

  @Column(name = "user_tenant_id")
  private String userTenantId;
}
