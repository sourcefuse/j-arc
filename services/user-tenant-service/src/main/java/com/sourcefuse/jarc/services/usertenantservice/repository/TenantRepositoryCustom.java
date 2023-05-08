package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;
import java.util.UUID;

public interface TenantRepositoryCustom<T> {

    void createCriteria(Class entityClass);

    List<T> nameByCourse(String author, String bookname, String lis, Class type);


    List<T> findByClientIdIn(List<String> allowedClients, Class type);

    List<UserView> findUserView(CriteriaQuery criteriaQuery);


    List<T> getAllUserView(UUID id, Class type);

    List<UserView> countUser(CriteriaQuery cq);

    T findById(UUID userId, UUID id, Class type);


}