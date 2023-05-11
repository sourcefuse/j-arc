package com.sourcefuse.jarc.services.usertenantservice.repository;

import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.UUID;

public interface TenantRepositoryCustom<T> {
  void createCriteria(Class<T> entityClass);

  List<T> findByClientIdIn(List<String> allowedClients, Class<T> type);

  List<T> findUserView(CriteriaQuery<T> criteriaQuery);

  List<T> getAllUserView(UUID id, Class<T> type);

  List<T> countUser(CriteriaQuery<T> cq);

  T findById(UUID userId, UUID id, Class<T> type);
}
