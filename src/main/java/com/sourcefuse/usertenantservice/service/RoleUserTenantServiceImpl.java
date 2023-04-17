package com.sourcefuse.usertenantservice.service;

import com.sourcefuse.usertenantservice.DTO.Count;
import com.sourcefuse.usertenantservice.DTO.UserTenant;
import com.sourcefuse.usertenantservice.repository.RoleUserTenantRepository;
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

    @Override
    public UserTenant save(UserTenant userTenant) {
        log.info(" :::: Create  UserTenant against Role Present::::");
        return roleUserTRepository.save(userTenant);

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
//      List<UserTenant> lst= roleUserTRepository.findUserTenantsByRoleId(id);
//        if(lst!=null && lst.size()>0){
//            roleUserTRepository.deleteByRoleId(id);
//            log.info("::::: Records deleted agaist the role ID"+id);
//            return new Count((long) lst.size());
//        }
        log.info("::::: Delete  User-Tenants present against the role ID"+id);
         return  roleUserTRepository.deleteByRoleId(id);
    }
}
