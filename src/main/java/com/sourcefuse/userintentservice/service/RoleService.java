package com.sourcefuse.userintentservice.service;

import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.DTO.Tenant;
import com.sourcefuse.userintentservice.exceptions.ApiPayLoadException;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {
      Role save(Role role);


      public Long count() ;

      public List<Role> findAll();

      Optional<Role> findById(UUID id) throws ApiPayLoadException;

      Long updateAll(List<Role> role);

      Role update(Role source, Role target);

      void deleteById(UUID id);
}
