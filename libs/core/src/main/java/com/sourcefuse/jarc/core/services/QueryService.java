package com.sourcefuse.jarc.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.core.models.filters.Filter;
import com.sourcefuse.jarc.core.models.filters.IncludeRelation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class QueryService {

  @PersistenceContext
  private EntityManager entityManager;

  public <T> List<T> executeQuery(Filter filter, Class<T> entityClass) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
    Root<T> root = criteriaQuery.from(entityClass);

    List<Selection<?>> selections = generateFieldSelection(
      criteriaBuilder,
      filter,
      root,
      criteriaQuery
    );

    List<Predicate> predicates = generatePredicates(
      criteriaBuilder,
      filter,
      root,
      criteriaQuery
    );

    if (!selections.isEmpty()) {
      criteriaQuery.multiselect(selections);
    } else {
      criteriaQuery.select(root);
    }
    criteriaQuery
      .distinct(true)
      .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Predicate> generatePredicates(
    CriteriaBuilder criteriaBuilder,
    Filter filter,
    From<?, T> from,
    CriteriaQuery<?> criteriaQuery
  ) {
    // Apply WHERE clause
    List<Predicate> predicates = new ArrayList<>();

    for (Entry<String, Object> entry : filter.getWhere().entrySet()) {
      String fieldName = entry.getKey();
      Object fieldValue = entry.getValue();

      if (fieldValue instanceof Map) {
        Map<String, Object> fieldOperators = (Map<String, Object>) fieldValue;

        fieldOperators
          .entrySet()
          .stream()
          .forEach((Entry<String, Object> operatorEntry) -> {
            String operator = operatorEntry.getKey();
            Object value = operatorEntry.getValue();

            Expression<String> fieldPath = from.get(fieldName);
            predicates.add(
              getPredicate(criteriaBuilder, operator, fieldPath, value)
            );
          });
      } else {
        Expression<String> fieldPath = from.get(fieldName);
        predicates.add(criteriaBuilder.equal(fieldPath, fieldValue));
      }
    }

    filter
      .getInclude()
      .stream()
      .forEach((IncludeRelation includeRelation) -> {
        predicates.addAll(
          generatePredicates(
            criteriaBuilder,
            includeRelation.getScope(),
            from.join(includeRelation.getRelation(), JoinType.INNER),
            criteriaQuery
          )
        );
      });
    return predicates;
  }

  private <T> List<Selection<?>> generateFieldSelection(
    CriteriaBuilder criteriaBuilder,
    Filter filter,
    From<?, T> from,
    CriteriaQuery<?> criteriaQuery
  ) {
    List<Selection<?>> selections = new ArrayList<>();

    filter
      .getFields()
      .entrySet()
      .stream()
      .forEach((Entry<String, Boolean> entry) -> {
        String fieldName = entry.getKey();
        Boolean includeField = entry.getValue();
        if (includeField) {
          Selection<?> fieldPath = from.get(fieldName).alias(fieldName);
          selections.add(fieldPath);
        }
      });
    filter
      .getInclude()
      .stream()
      .forEach((IncludeRelation includeRelation) -> {
        selections.addAll(
          generateFieldSelection(
            criteriaBuilder,
            includeRelation.getScope(),
            from.join(includeRelation.getRelation(), JoinType.INNER),
            criteriaQuery
          )
        );
      });
    return selections;
  }

  private Predicate getPredicate(
    CriteriaBuilder criteriaBuilder,
    String operator,
    Expression<String> fieldPath,
    Object value
  ) {
    Predicate predicate;
    Double doubleValue;
    Expression<Double> field;
    switch (operator) {
      case "eq":
        predicate = criteriaBuilder.equal(fieldPath, value);
        break;
      case "ne":
        predicate = criteriaBuilder.notEqual(fieldPath, value);
        break;
      case "gt":
        doubleValue = Double.valueOf(value.toString());
        field = fieldPath.as(Double.class);
        predicate = criteriaBuilder.greaterThan(field, doubleValue);
        break;
      case "gte":
        doubleValue = Double.valueOf(value.toString());
        field = fieldPath.as(Double.class);
        predicate = criteriaBuilder.greaterThanOrEqualTo(field, doubleValue);
        break;
      case "lt":
        doubleValue = Double.valueOf(value.toString());
        field = fieldPath.as(Double.class);
        predicate = criteriaBuilder.lessThan(field, doubleValue);
        break;
      case "lte":
        doubleValue = Double.valueOf(value.toString());
        field = fieldPath.as(Double.class);
        predicate = criteriaBuilder.lessThanOrEqualTo(field, doubleValue);
        break;
      case "like":
        predicate = criteriaBuilder.like(fieldPath, "%" + value + "%");
        break;
      case "notlike":
        predicate = criteriaBuilder.notLike(fieldPath, "%" + value + "%");
        break;
      default:
        throw new IllegalArgumentException("Unsupported operator: " + operator);
    }
    return predicate;
  }
}
