package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Role;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;


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
