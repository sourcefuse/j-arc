package com.sourcefuse.userintentservice.repository;

import com.sourcefuse.userintentservice.DTO.Tenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Tenant, String>
{    
}    