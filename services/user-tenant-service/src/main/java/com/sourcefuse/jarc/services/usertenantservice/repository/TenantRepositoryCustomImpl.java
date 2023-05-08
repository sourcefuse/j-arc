package com.sourcefuse.jarc.services.usertenantservice.repository;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import com.sourcefuse.jarc.services.usertenantservice.commons.CommonConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
class TenantRepositoryCustomImpl<T> implements TenantRepositoryCustom<T> {
    CriteriaBuilder cb;
    CriteriaQuery<T> criteriaQuery;
    Root<T> root;
    private EntityManager em;

    @Override
    public void createCriteria(Class entityClass) {

        this.cb = this.em.getCriteriaBuilder();
        criteriaQuery = cb.createQuery(entityClass);
        root = criteriaQuery.from(entityClass);
    }

    @Override
    public List<T> nameByCourse(String author, String bookname, String lis, Class type) {
        createCriteria(type);
        Predicate authorNamePredicate = cb.equal(root.get("author"), author);
        Predicate titlePredicate = cb.like(root.get("lName"), "%" + bookname + "%");
        List<String> ls = Arrays.asList(new String[]{lis, "320"});
        Predicate inPredi = root.get("price").in(ls);
        Predicate finalPr = cb.or(inPredi, cb.and(authorNamePredicate, titlePredicate));
        //Using criteria builder you can build your criteria queries.
        criteriaQuery.where(finalPr);

        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<T> findByClientIdIn(List<String> allowedClients, Class type) {
        createCriteria(type);
        Predicate inPre = root.get("allowedClients").in(allowedClients);
        criteriaQuery.where(inPre);
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<UserView> findUserView(CriteriaQuery criteriaQuery) {

        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<T> getAllUserView(UUID id, Class type) {
        createCriteria(type);
        criteriaQuery.where(cb.equal(root.get("tenantId"), id), cb.notEqual(root.get("roleType"), CommonConstants.superAdminRoleType));
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<UserView> countUser(CriteriaQuery cq) {
        return em.createQuery(cq).getResultList();
    }

    @Override
    public T findById(UUID userId, UUID id, Class type) {
        createCriteria(type);
        criteriaQuery.where(cb.equal(root.get("id"), userId), cb.equal(root.get("tenantId"), id));
        return em.createQuery(criteriaQuery).getSingleResult();
    }


}