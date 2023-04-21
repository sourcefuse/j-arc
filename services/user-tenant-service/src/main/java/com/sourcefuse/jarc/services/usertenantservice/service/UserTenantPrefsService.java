package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenantPrefs;

import java.util.List;
import java.util.UUID;

public interface UserTenantPrefsService {
    UserTenantPrefs findByUserTenantIdConfKey(UUID id, String configKey);
    public UserTenantPrefs save(UserTenantPrefs userTenantPrefs);


    List<UserTenantPrefs> findAll();
}
