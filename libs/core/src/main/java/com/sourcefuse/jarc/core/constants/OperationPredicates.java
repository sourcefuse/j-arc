package com.sourcefuse.jarc.core.constants;

import com.sourcefuse.jarc.core.interfaces.TriFunction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OperationPredicates {

  private static Map<String, 
  TriFunction<CriteriaBuilder, Expression<String>, Object, Predicate>>
   operatorMap = new HashMap<>();

  public static final String AND_OPERATOR = "and";
  public static final String OR_OPERATOR = "or";

  private OperationPredicates() {
    throw new IllegalStateException("Utility class");
  }

  private static void setUpOperators() {
    setUpEqualsAndInOperators();
    setUpLikeOperators();
    setUpGreaterThanOperators();
    setUpLessThanOperators();
  }

  private static void setUpEqualsAndInOperators() {
    operatorMap = new HashMap<>();
    operatorMap.put("eq", CriteriaBuilder::equal);
    operatorMap.put("neq", CriteriaBuilder::notEqual);
    operatorMap.put(
      "in",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        path.in((List<?>) value)
    );
    operatorMap.put(
      "nin",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        criteriaBuilder.not(path.in((List<?>) value))
    );
  }

  private static void setUpLikeOperators() {
    operatorMap.put(
      "like",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        criteriaBuilder.like(path, "%" + value + "%")
    );
    operatorMap.put(
      "nlike",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        criteriaBuilder.notLike(path, "%" + value + "%")
    );
  }

  private static void setUpGreaterThanOperators() {
    operatorMap.put(
      "gt",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        criteriaBuilder.greaterThan(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
    operatorMap.put(
      "gte",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        criteriaBuilder.greaterThanOrEqualTo(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
  }

  private static void setUpLessThanOperators() {
    operatorMap.put(
      "lt",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
        criteriaBuilder.lessThan(
          path.as(Double.class),
          Double.valueOf(value.toString())
        )
    );
    operatorMap.put(
      "lte",
      (
          CriteriaBuilder criteriaBuilder,
          Expression<String> path,
          Object value
        ) ->
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
        (CriteriaBuilder builder, Expression<String> path, Object val) -> {
          throw new IllegalArgumentException(
            "Unsupported operator: " + operator
          );
        }
      )
      .apply(criteriaBuilder, fieldPath, value);
  }
}
