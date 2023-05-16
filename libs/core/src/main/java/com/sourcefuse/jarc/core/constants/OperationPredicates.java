package com.sourcefuse.jarc.core.constants;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface TriFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}

public class OperationPredicates {

  private static Map<String, TriFunction<CriteriaBuilder, Expression<String>, Object, Predicate>> operatorMap =
    new HashMap<>();

  private static void setUpOperators() {
    operatorMap = new HashMap<>();
    operatorMap.put(
      "eq",
      (criteriaBuilder, path, value) -> criteriaBuilder.equal(path, value)
    );
    operatorMap.put(
      "neq",
      (criteriaBuilder, path, value) -> criteriaBuilder.notEqual(path, value)
    );
    operatorMap.put(
      "in",
      (criteriaBuilder, path, value) -> path.in((List<?>) value)
    );
    operatorMap.put(
      "nin",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.not(path.in((List<?>) value))
    );
    operatorMap.put(
      "like",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.like(path, "%" + value + "%")
    );
    operatorMap.put(
      "nlike",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.notLike(path, "%" + value + "%")
    );
    operatorMap.put(
      "gt",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.greaterThan(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
    operatorMap.put(
      "gte",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.greaterThanOrEqualTo(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
    operatorMap.put(
      "lt",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.lessThan(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
    operatorMap.put(
      "lte",
      (criteriaBuilder, path, value) ->
        criteriaBuilder.lessThanOrEqualTo(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
  }

  public static Predicate getPredicate(
    CriteriaBuilder criteriaBuilder,
    String operator,
    Expression<String> fieldPath,
    Object value
  ) {
    setUpOperators();
    return operatorMap
      .getOrDefault(
        operator,
        (builder, path, val) -> {
          throw new IllegalArgumentException(
            "Unsupported operator: " + operator
          );
        }
      )
      .apply(criteriaBuilder, fieldPath, value);
  }

  private OperationPredicates() {
    throw new IllegalStateException("Utility class");
  }
}
