package com.sourcefuse.example.authenticationaudit.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.sourcefuse.example.authenticationaudit.models.Todo;

public interface TodoRepository extends CrudRepository<Todo, UUID> {

}
