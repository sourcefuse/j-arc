package com.sourcefuse.jarc.core.repositories.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@SuppressWarnings("rawtypes")
public final class ByIdsSpecification<T> implements Specification<T> {

  private static final long serialVersionUID = -3244704710376973492L;

  private final JpaEntityInformation<T, ?> entityInformation;

  public static ParameterExpression<Iterable> parameter;

  public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
    this.entityInformation = entityInformation;
  }

  @Override
  public Predicate toPredicate(
    Root<T> root,
    CriteriaQuery<?> query,
    CriteriaBuilder cb
  ) {
    Path<?> path = root.get(entityInformation.getIdAttribute());
    parameter = cb.parameter(Iterable.class);
    return path.in(parameter);
  }
}
