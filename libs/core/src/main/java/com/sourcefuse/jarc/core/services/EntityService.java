//package com.sourcefuse.jarc.core.services;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.JoinType;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//
//class Filter {
//	Map<String, Object> where;
//	Map<String, Boolean> fileds;
//	List<Map<String, Object>> includes;
//}
//
//@Service
//public class EntityService {
//
//	@Autowired
//	private EntityManager entityManager;
//
//	public <T> List<T> find(Class<T> entityClass, Filter filter) {
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
//		Root<T> root = criteriaQuery.from(entityClass);
//
//		// Add WHERE clause
//		Predicate predicate = buildPredicate(criteriaBuilder, root, filter.getWhere());
//		if (predicate != null) {
//			criteriaQuery.where(predicate);
//		}
//
//		// Add SELECT clause
//		criteriaQuery.select(buildSelection(criteriaBuilder, root, filter.getFields()));
//
//		// Add JOIN clauses
//		for (Include include : filter.getInclude()) {
//			Fetch<?, ?> fetch = root.fetch(include.getRelation(), JoinType.LEFT);
//			if (include.getWhere() != null) {
//				fetch.fetch(include.getRelation(), JoinType.LEFT)
//						.where(buildPredicate(criteriaBuilder, (Root<?>) fetch, include.getWhere()));
//			}
//			if (include.getFields() != null) {
//				fetch.select(buildSelection(criteriaBuilder, (Root<?>) fetch, include.getFields()));
//			}
//			if (include.getOrder() != null) {
//				fetch.orderBy(buildOrder(criteriaBuilder, (Root<?>) fetch, include.getOrder()));
//			}
//			for (Include innerInclude : include.getInclude()) {
//				Fetch<?, ?> innerFetch = fetch.fetch(innerInclude.getRelation(), JoinType.LEFT);
//				if (innerInclude.getWhere() != null) {
//					innerFetch.fetch(innerInclude.getRelation(), JoinType.LEFT)
//							.where(buildPredicate(criteriaBuilder, (Root<?>) innerFetch, innerInclude.getWhere()));
//				}
//				if (innerInclude.getFields() != null) {
//					innerFetch.select(buildSelection(criteriaBuilder, (Root<?>) innerFetch, innerInclude.getFields()));
//				}
//				if (innerInclude.getOrder() != null) {
//					innerFetch.orderBy(buildOrder(criteriaBuilder, (Root<?>) innerFetch, innerInclude.getOrder()));
//				}
//			}
//		}
//
//		return entityManager.createQuery(criteriaQuery).getResultList();
//	}
//
//	private <T> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<T> root, Object where) {
//		if (where == null) {
//			return null;
//		} else if (where instanceof Predicate) {
//			return (Predicate) where;
//		} else {
//			return buildPredicate(criteriaBuilder, root, Filter.from(where));
//		}
//	}
//}
