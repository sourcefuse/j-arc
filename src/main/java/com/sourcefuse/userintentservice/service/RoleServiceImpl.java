package com.sourcefuse.userintentservice.service;

import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.commonutils.CommonUtils;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;
import com.sourcefuse.userintentservice.repository.RoleRepository;
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
        return roleRepository.save(role);
    }

    @Override
    public Long count() {
        return roleRepository.count();
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) throws ApiPayLoadException {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role;
        } else
            throw new ApiPayLoadException("No role is present against given value");

    }

    @Override
    public Long updateAll(List<Role> role) {

        return (long) roleRepository.saveAll(role).size();
    }

    @Override
    public Role update(Role source, Role target) {
        new CommonUtils<Role>().copyProperties(source,target);
        log.info("updated values in models are :::::::"+target.toString());
        return roleRepository.save(target);
    }

    @Override
    public void deleteById(UUID id) {
        roleRepository.deleteById(id);
    }


}
