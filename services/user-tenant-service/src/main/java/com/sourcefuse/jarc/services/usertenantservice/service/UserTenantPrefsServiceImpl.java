package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenantPrefs;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantPrefsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTenantPrefsServiceImpl implements UserTenantPrefsService{

    private  final UserTenantPrefsRepository userTPRepository;
    @Override
    public UserTenantPrefs findByUserTenantIdConfKey(UUID id, String configKey) {
        return userTPRepository.findByUserTenantIdConfKey(id,configKey);
    }
    @Override
    public UserTenantPrefs save(UserTenantPrefs userTenantPrefs) {
        log.info(" ::::  Create User - Tenant - Prefers   ::::");
        return userTPRepository.save(userTenantPrefs);
    }
    @Override
    public List<UserTenantPrefs> findAll() {
        log.info(" :::: Fetch  total no of  User -Tenant -Prefs  Present ::::");
        return userTPRepository.findAll();
    }

}
