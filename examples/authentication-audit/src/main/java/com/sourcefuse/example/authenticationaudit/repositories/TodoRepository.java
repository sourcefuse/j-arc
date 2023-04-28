package com.sourcefuse.example.authenticationaudit.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sourcefuse.example.authenticationaudit.models.Todo;
import com.sourcefuse.jarc.services.auditservice.audit.softdelete.SoftDeletesRepository;

public interface TodoRepository extends SoftDeletesRepository<Todo, UUID> {

}
