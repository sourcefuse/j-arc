package com.sourcefuse.jarc.services.usertenantservice.views;

import com.sourcefuse.jarc.services.usertenantservice.dto.Group;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "v_user_group")
public class UserGroupView extends Group {

  private static final long serialVersionUID = 1905122041950251214L;

  @Column(name = "user_tenant_id")
  private UUID userTenantId;
}
