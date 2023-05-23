package com.sourcefuse.jarc.core.filters.constants;

import static java.util.Map.entry;

import com.sourcefuse.jarc.core.filters.interfaces.TriFunction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

public final class OperationPredicates {

  private static Map<String, TriFunction<CriteriaBuilder, 
  Expression<String>, Object, Predicate>> operatorMap =
    Map.ofEntries(
      entry("eq", CriteriaBuilder::equal),
      entry("neq", CriteriaBuilder::notEqual),
      entry(
        "in",
        (
            CriteriaBuilder criteriaBuilder,
            Expression<String> path,
            Object value
          ) ->
          path.in((List<?>) value)
      ),
      entry(
        "nin",
        (
            CriteriaBuilder criteriaBuilder,
            Expression<String> path,
            Object value
          ) ->
          criteriaBuilder.not(path.in((List<?>) value))
      ),
      entry(
        "like",
        (
            CriteriaBuilder criteriaBuilder,
            Expression<String> path,
            Object value
          ) ->
          criteriaBuilder.like(path, "%" + value + "%")
      ),
      entry(
        "nlike",
        (
            CriteriaBuilder criteriaBuilder,
            Expression<String> path,
            Object value
          ) ->
          criteriaBuilder.notLike(path, "%" + value + "%")
      ),
      entry(
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
      ),
      entry(
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
      ),
      entry(
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
      ),
      entry(
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
      )
    );

  public static final String AND_OPERATOR = "and";
  public static final String OR_OPERATOR = "or";

  private OperationPredicates() {
    throw new IllegalStateException("Utility class");
  }

  public static Predicate getPredicate(
    CriteriaBuilder criteriaBuilder,
    String operator,
    Expression<String> fieldPath,
    Object value
  ) {
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
