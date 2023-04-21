package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Role;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        log.info(" ::::  Create Role  ::::");
        return roleRepository.save(role);
    }

    @Override
    public Long count() {
        log.info(" :::: Count  total no of  Role  Present ::::");
        return roleRepository.count();
    }

    @Override
    public List<Role> findAll() {
        log.info(" :::: Fetch  total no of  Role  Present ::::");
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) throws ApiPayLoadException {
        log.info(" :::: Fetch  Role  Present against Id ::::"+id);
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role;
        } else
            throw new ApiPayLoadException("No role is present against given value");

    }

    @Override
    public Long updateAll(List<Role> role) {
        log.info(" :::: Update  total no of  Role  Present ::::");
        return (long) roleRepository.saveAll(role).size();
    }

    @Override
    public Role update(Role source, Role target) {
        log.info(" :::: Patch total no of  Role  Present ::::");
        new CommonUtils<Role>().copyProperties(source,target);
        log.info("updated values in models are :::::::"+target.toString());
        return roleRepository.save(target);
    }

    @Override
    public void deleteById(UUID id) {

        log.info(" :::: Delete  Role  Present against Id::::"+id);
        roleRepository.deleteById(id);
    }


}
