package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Group;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupService {

    Group save(Group group);

    Long count();

    List<Group> findAll();

    Optional<Group> findById(UUID id) throws ApiPayLoadException;

    Group update(Group source, Group target);

    void deleteById(UUID id);
}
