package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service

public class RoleUserTenantServiceImpl implements RoleUserTenantService {


    private final RoleUserTenantRepository roleUserTRepository;

    private  final UserViewRepository userViewRepository;

    @Override
    public UserTenant save(UserTenant userTenant) {
        log.info(" :::: Create  UserTenant against Role Present::::");
        return roleUserTRepository.save(userTenant);

    }

    @Override
    public UserTenant findById(UUID id) {
        log.info(" :::: Fetch UserTenant against the Id::::"+id);
        return roleUserTRepository.findById(id);
    }

    @Override
    public List<UserTenant> findUserTenantsByRoleId(UUID id) {
        log.info(" :::: Fetch  UserTenant For Role Present against roleId::::"+id);
        return roleUserTRepository.findUserTenantsByRoleId(id);
    }

    @Override
    public Long updateAll(List<UserTenant> userTenants) {
        log.info(" :::: Update  UserTenant For Role Present against ::::");
        return (long) roleUserTRepository.saveAll(userTenants).size();
    }

    @Override
    @Transactional
    public Count DeleteUserTenantsByRoleId(UUID id) {
        log.info("::::: Delete  User-Tenants present against the role ID"+id);

                long count= roleUserTRepository.deleteByRoleId(id);
                return new Count(count);
    }

    @Override
    public UserView getUserView(UUID id) {
        return userViewRepository.findByUserTenantId(id);
    }
}

