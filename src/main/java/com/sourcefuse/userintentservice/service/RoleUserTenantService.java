package com.sourcefuse.userintentservice.service;

import com.sourcefuse.userintentservice.DTO.Count;
import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.DTO.UserTenant;

import java.util.List;
import java.util.UUID;

public interface RoleUserTenantService {
    UserTenant save(UserTenant userTenant);


    List<UserTenant> findUserTenantsByRoleId(UUID id);

    Long updateAll(List<UserTenant> tarUserTenantArrayList);

    Count DeleteUserTenantsByRoleId(UUID id);
}
