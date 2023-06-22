package com.sourcefuse.jarc.core.filters.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.constants.Logger;
import com.sourcefuse.jarc.core.filters.constants.OperationPredicates;
import com.sourcefuse.jarc.core.filters.models.Filter;
import com.sourcefuse.jarc.core.filters.models.IncludeRelation;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class QueryService {

  @PersistenceContext
  private EntityManager entityManager;

  public <T> List<T> executeQuery(String filterJson, Class<T> entityClass) {
    ObjectMapper objectMapper = new ObjectMapper();
    Filter filter;
    try {
      filter = objectMapper.readValue(filterJson, Filter.class);
    } catch (JsonProcessingException e) {
      Logger.error(e);
      throw new IllegalArgumentException(
        "provided json is not valid: " + filterJson
      );
    }
    return this.executeQuery(filter, entityClass);
  }

  public <T> List<T> executeQuery(Filter filter, Class<T> entityClass) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
    Root<T> root = criteriaQuery.from(entityClass);

    List<Selection<?>> selections = generateFieldSelection(filter, root);

    List<Predicate> predicates = buildPredicates(
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

  private <T> List<Predicate> buildPredicates(
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
        predicates.addAll(
          generatePredicates(criteriaBuilder, from, fieldName, fieldValue)
        );
      } else {
        Expression<String> fieldPath = from.get(fieldName);
        predicates.add(criteriaBuilder.equal(fieldPath, fieldValue));
      }
    }

    filter
      .getInclude()
      .stream()
      .forEach((IncludeRelation includeRelation) ->
        predicates.addAll(
          buildPredicates(
            criteriaBuilder,
            includeRelation.getScope(),
            from.join(includeRelation.getRelation(), JoinType.INNER),
            criteriaQuery
          )
        )
      );
    return predicates;
  }

  private static <T> List<Selection<?>> generateFieldSelection(
    Filter filter,
    From<?, T> from
  ) {
    List<Selection<?>> selections = new ArrayList<>();

    filter
      .getFields()
      .entrySet()
      .stream()
      .forEach((Entry<String, Boolean> entry) -> {
        String fieldName = entry.getKey();
        boolean includeField = entry.getValue();
        if (includeField) {
          Selection<?> fieldPath = from.get(fieldName).alias(fieldName);
          selections.add(fieldPath);
        }
      });
    return selections;
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Predicate> generatePredicates(
    CriteriaBuilder criteriaBuilder,
    From<?, T> from,
    String fieldName,
    Object fieldValue
  ) {
    List<Predicate> predicates = new ArrayList<>();
    if ("and".equals(fieldName)) {
      List<Predicate> andPredicates = new ArrayList<>();
      Map<String, Object> fieldOperators = (Map<String, Object>) fieldValue;
      fieldOperators
        .entrySet()
        .stream()
        .forEach((Entry<String, Object> operatorEntry) -> {
          String operator = operatorEntry.getKey();
          Object value = operatorEntry.getValue();
          andPredicates.addAll(
            generatePredicates(criteriaBuilder, from, operator, value)
          );
        });
      predicates.add(
        criteriaBuilder.and(andPredicates.toArray(new Predicate[0]))
      );
    } else if ("or".equals(fieldName)) {
      List<Predicate> orPredicates = new ArrayList<>();
      Map<String, Object> fieldOperators = (Map<String, Object>) fieldValue;
      fieldOperators
        .entrySet()
        .stream()
        .forEach((Entry<String, Object> operatorEntry) -> {
          String operator = operatorEntry.getKey();
          Object value = operatorEntry.getValue();
          orPredicates.addAll(
            generatePredicates(criteriaBuilder, from, operator, value)
          );
        });
      predicates.add(
        criteriaBuilder.or(orPredicates.toArray(new Predicate[0]))
      );
    } else {
      predicates.addAll(
        generatePredicatesByOperators(
          criteriaBuilder,
          from,
          fieldName,
          fieldValue
        )
      );
    }
    return predicates;
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Predicate> generatePredicatesByOperators(
    CriteriaBuilder criteriaBuilder,
    From<?, T> from,
    String fieldName,
    Object fieldValue
  ) {
    List<Predicate> predicates = new ArrayList<>();
    if (fieldValue instanceof Map) {
      Map<String, Object> fieldOperators = (Map<String, Object>) fieldValue;
      predicates.addAll(
        fieldOperators
          .entrySet()
          .stream()
          .map((Entry<String, Object> operatorEntry) ->
            OperationPredicates.getPredicate(
              criteriaBuilder,
              operatorEntry.getKey(),
              from.get(fieldName),
              operatorEntry.getValue()
            )
          )
          .toList()
      );
    } else {
      Expression<String> fieldPath = from.get(fieldName);
      predicates.add(criteriaBuilder.equal(fieldPath, fieldValue));
    }
    return predicates;
  }
}
