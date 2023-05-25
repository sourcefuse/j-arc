package com.sourcefuse.jarc.services.featuretoggleservice.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.featuretoggleservice.model.Todo;

@Repository
public interface TodoRepository extends SoftDeletesRepository<Todo, UUID> {

}
