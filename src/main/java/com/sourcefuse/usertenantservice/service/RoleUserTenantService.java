package com.sourcefuse.usertenantservice.service;

import com.sourcefuse.usertenantservice.DTO.Count;
import com.sourcefuse.usertenantservice.DTO.UserTenant;

import java.util.List;
import java.util.UUID;

public interface RoleUserTenantService {
    UserTenant save(UserTenant userTenant);


    List<UserTenant> findUserTenantsByRoleId(UUID id);

    Long updateAll(List<UserTenant> tarUserTenantArrayList);

    Count DeleteUserTenantsByRoleId(UUID id);
}
