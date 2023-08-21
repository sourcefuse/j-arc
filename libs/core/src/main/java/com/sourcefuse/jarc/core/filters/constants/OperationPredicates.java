package com.sourcefuse.jarc.core.filters.constants;

import static java.util.Map.entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sourcefuse.jarc.core.filters.interfaces.TriFunction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OperationPredicates {

  private static ObjectMapper objectMapper = new ObjectMapper()
    .registerModule(new JavaTimeModule());

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static Map<String, TriFunction<CriteriaBuilder,
   Expression<?>, Object, Predicate>> operatorMap =
    Map.ofEntries(
      entry("eq", CriteriaBuilder::equal),
      entry("neq", CriteriaBuilder::notEqual),
      entry(
        "in",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          path.in((List<?>) value)
      ),
      entry(
        "nin",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.not(path.in((List<?>) value))
      ),
      entry(
        "like",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.like(path.as(String.class), String.valueOf(value))
      ),
      entry(
        "nlike",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.notLike(path.as(String.class), String.valueOf(value))
      ),
      entry(
        "gt",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.greaterThan(
            (Expression<? extends Comparable>) path,
            (Comparable) value
          )
      ),
      entry(
        "gte",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.greaterThanOrEqualTo(
            (Expression<? extends Comparable>) path,
            (Comparable) value
          )
      ),
      entry(
        "lt",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.lessThan(
            (Expression<? extends Comparable>) path,
            (Comparable) value
          )
      ),
      entry(
        "lte",
        (CriteriaBuilder cb, Expression<?> path, Object value) ->
          cb.lessThanOrEqualTo(
            (Expression<? extends Comparable>) path,
            (Comparable) value
          )
      )
    );

  public static final String AND_OPERATOR = "and";
  public static final String OR_OPERATOR = "or";

  private OperationPredicates() {
    throw new IllegalStateException("Utility class");
  }

  public static Predicate getPredicate(
    CriteriaBuilder cb,
    String operator,
    Expression<?> fieldPath,
    Object value
  ) {
    return operatorMap
      .getOrDefault(
        operator,
        (CriteriaBuilder builder, Expression<?> path, Object val) -> {
          throw new IllegalArgumentException(
            "Unsupported operator: " + operator
          );
        }
      )
      .apply(cb, fieldPath, castValueToDataType(fieldPath, value));
  }

  private static Object castValueToDataType(
    Expression<?> path,
    Object value
  ) {
    Class<?> dataType = path.getJavaType();

    if (value instanceof List<?> listVal) {
      return listVal
        .stream()
        .map(ele -> objectMapper.convertValue(ele, dataType))
        .toList();
    } else if (value instanceof Set<?> setVal) {
      return setVal
        .stream()
        .map(ele -> objectMapper.convertValue(ele, dataType))
        .toList();
    } else {
      return objectMapper.convertValue(value, dataType);
    }
  }
}
