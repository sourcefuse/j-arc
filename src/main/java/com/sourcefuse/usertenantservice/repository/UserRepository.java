package com.sourcefuse.usertenantservice.repository;

import com.sourcefuse.usertenantservice.DTO.Tenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Tenant, String>
{    
}    