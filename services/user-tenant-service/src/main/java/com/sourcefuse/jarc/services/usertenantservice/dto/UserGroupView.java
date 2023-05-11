package com.sourcefuse.jarc.services.usertenantservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "v_user_group")
public class UserGroupView extends Group {

  private static final long serialVersionUID = 1905122041950251214L;

  @Column(name = "user_tenant_id")
  private String userTenantId;
}
