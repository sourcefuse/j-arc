package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;

import java.util.List;
import java.util.UUID;

public interface RoleUserTenantService {
    UserTenant save(UserTenant userTenant);

    UserTenant findById(UUID id);
    List<UserTenant> findUserTenantsByRoleId(UUID id);

    Long updateAll(List<UserTenant> tarUserTenantArrayList);

    Count DeleteUserTenantsByRoleId(UUID id);

    /***Method to fetch UserView based on UserTenant ID*/
    public UserView getUserView(UUID id);

}
