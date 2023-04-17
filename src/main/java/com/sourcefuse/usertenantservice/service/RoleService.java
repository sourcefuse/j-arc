package com.sourcefuse.usertenantservice.service;

import com.sourcefuse.usertenantservice.DTO.Role;
import com.sourcefuse.usertenantservice.exceptions.ApiPayLoadException;


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
