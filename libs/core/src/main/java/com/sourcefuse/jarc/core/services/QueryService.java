package com.sourcefuse.jarc.core.services;

import com.sourcefuse.jarc.core.models.filters.Filter;
import com.sourcefuse.jarc.core.models.filters.IncludeRelation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

  @PersistenceContext
  private EntityManager entityManager;

  CriteriaBuilder criteriaBuilder;

  public QueryService() {}

  public <T> List<T> executeQuery(Filter filter, Class<T> entityClass) {
    this.criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
    Root<T> root = criteriaQuery.from(entityClass);

    List<Selection<?>> selections = generateFieldSelection(
      filter,
      root,
      criteriaQuery
    );

    List<Predicate> predicates = generatePredicates(
      filter,
      root,
      criteriaQuery
    );

    /**
     * TODO: field selection is not working if (selections.size() > 0) {
     * criteriaQuery.multiselect(selections); } else {
     */
    criteriaQuery.select(root);

    criteriaQuery
      .distinct(true)
      .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Predicate> generatePredicates(
    Filter filter,
    From<?, T> from,
    CriteriaQuery<?> criteriaQuery
  ) {
    // Apply WHERE clause
    List<Predicate> predicates = new ArrayList<>();
    if (
      filter != null &&
      filter.getWhere() != null &&
      !filter.getWhere().isEmpty()
    ) {
      for (Map.Entry<String, Object> entry : filter.getWhere().entrySet()) {
        String fieldName = entry.getKey();
        Object fieldValue = entry.getValue();

        if (fieldValue instanceof Map) {
          // Handle operators for the field
          Map<String, Object> fieldOperators = (Map<String, Object>) fieldValue;

          for (Map.Entry<String, Object> operatorEntry : fieldOperators.entrySet()) {
            String operator = operatorEntry.getKey();
            Object value = operatorEntry.getValue();

            Expression<String> fieldPath = from.get(fieldName);
            predicates.add(getPredicate(operator, fieldPath, value));
          }
        } else {
          Expression<String> fieldPath = from.get(fieldName);
          predicates.add(criteriaBuilder.equal(fieldPath, fieldValue));
        }
      }
      if (filter.getInclude() != null && filter.getInclude().size() > 0) {
        for (IncludeRelation includeRelation : filter.getInclude()) {
          String relation = includeRelation.getRelation();
          Filter relationFilter = includeRelation.getScope();

          Join<Object, Object> join = from.join(relation, JoinType.INNER);
          predicates.addAll(
            generatePredicates(relationFilter, join, criteriaQuery)
          );
        }
      }
    }
    return predicates;
  }

  private <T> List<Selection<?>> generateFieldSelection(
    Filter filter,
    From<?, T> from,
    CriteriaQuery<?> criteriaQuery
  ) {
    List<Selection<?>> selections = new ArrayList<>();
    if (
      filter != null &&
      filter.getFields() != null &&
      !filter.getFields().isEmpty()
    ) {
      for (Map.Entry<String, Boolean> entry : filter.getFields().entrySet()) {
        String fieldName = entry.getKey();
        Boolean includeField = entry.getValue();

        if (includeField) {
          Path<?> fieldPath = from.get(fieldName);
          selections.add(fieldPath);
        }
      }

      if (filter.getInclude() != null && filter.getInclude().size() > 0) {
        for (IncludeRelation includeRelation : filter.getInclude()) {
          String relation = includeRelation.getRelation();
          Filter relationFilter = includeRelation.getScope();

          Join<Object, Object> join = from.join(relation);
          selections.addAll(
            generateFieldSelection(relationFilter, join, criteriaQuery)
          );
        }
      }
    }
    return selections;
  }

  private Predicate getPredicate(
    String operator,
    Expression<String> fieldPath,
    Object value
  ) {
    switch (operator) {
      case "eq":
        return criteriaBuilder.equal(fieldPath, value);
      case "ne":
        return criteriaBuilder.notEqual(fieldPath, value);
      case "gt":
        return criteriaBuilder.greaterThan(
          fieldPath.as(Double.class),
          Double.valueOf(value.toString())
        );
      case "gte":
        return criteriaBuilder.greaterThanOrEqualTo(
          fieldPath.as(Double.class),
          Double.valueOf(value.toString())
        );
      case "lt":
        return criteriaBuilder.lessThan(
          fieldPath.as(Double.class),
          Double.valueOf(value.toString())
        );
      case "lte":
        return criteriaBuilder.lessThanOrEqualTo(
          fieldPath.as(Double.class),
          Double.valueOf(value.toString())
        );
      case "like":
        return criteriaBuilder.like(fieldPath, "%" + value + "%");
      case "notlike":
        return criteriaBuilder.notLike(fieldPath, "%" + value + "%");
      default:
        throw new IllegalArgumentException("Unsupported operator: " + operator);
    }
  }
}
