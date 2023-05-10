package com.sourcefuse.jarc.core.repositories.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public final class ByIdSpecification<T, ID> implements Specification<T> {

  private static final long serialVersionUID = 4888951075373995827L;

  private final JpaEntityInformation<T, ?> entityInformation;
  private final ID id;

  public ByIdSpecification(
    JpaEntityInformation<T, ?> entityInformation,
    ID id
  ) {
    this.entityInformation = entityInformation;
    this.id = id;
  }

  @Override
  public Predicate toPredicate(
    Root<T> root,
    CriteriaQuery<?> query,
    CriteriaBuilder cb
  ) {
    String idAttributeName;
    SingularAttribute<? super T, ?> idAttribute =
      entityInformation.getIdAttribute();
    if (idAttribute != null) {
      idAttributeName = idAttribute.getName();
    } else {
      throw new IllegalArgumentException(
        "entityInformation getIdAttribute is null"
      );
    }
    return cb.equal(root.<ID>get(idAttributeName), id);
  }
}
