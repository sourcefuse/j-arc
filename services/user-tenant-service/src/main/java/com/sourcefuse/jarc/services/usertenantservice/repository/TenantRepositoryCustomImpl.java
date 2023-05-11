package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.commons.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TenantRepositoryCustomImpl<T> implements TenantRepositoryCustom<T> {

  CriteriaBuilder cb;
  CriteriaQuery<T> criteriaQuery;
  Root<T> root;

  @PersistenceContext
  private EntityManager em;

  @Override
  public void createCriteria(Class<T> entityClass) {
    this.cb = this.em.getCriteriaBuilder();
    criteriaQuery = cb.createQuery(entityClass);
    root = criteriaQuery.from(entityClass);
  }

  @Override
  public List<T> findByClientIdIn(List<String> allowedClients, Class<T> type) {
    createCriteria(type);
    Predicate inPre = root.get("clientId").in(allowedClients);
    criteriaQuery.where(inPre);
    return em.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public List<T> findUserView(CriteriaQuery<T> criteriaQuery) {
    return em.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public List<T> getAllUserView(UUID id, Class<T> type) {
    createCriteria(type);
    criteriaQuery.where(
      cb.equal(root.get("tenantId"), id),
      cb.notEqual(root.get("roleType"), CommonConstants.SUPER_ADMIN_ROLE_TYPE)
    );
    return em.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public List<T> countUser(CriteriaQuery<T> cq) {
    return em.createQuery(cq).getResultList();
  }

  @Override
  public T findById(UUID userId, UUID id, Class<T> type) {
    createCriteria(type);
    criteriaQuery.where(
      cb.equal(root.get("id"), userId),
      cb.equal(root.get("tenantId"), id)
    );
    return em.createQuery(criteriaQuery).getSingleResult();
  }
}
