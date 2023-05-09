package com.sourcefuse.jarc.core.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

  @PersistenceContext
  EntityManager entityManager;
}
