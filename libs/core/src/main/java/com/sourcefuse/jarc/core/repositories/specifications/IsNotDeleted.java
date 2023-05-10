package com.sourcefuse.jarc.core.repositories.specifications;

import com.sourcefuse.jarc.core.constants.SoftDeleteRepositoryConstants;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public final class IsNotDeleted<T> implements Specification<T> {

  private static final long serialVersionUID = 8052101109493836760L;

  @Override
  public Predicate toPredicate(
    Root<T> root,
    CriteriaQuery<?> query,
    CriteriaBuilder cb
  ) {
    return cb.isFalse(
      root.<Boolean>get(SoftDeleteRepositoryConstants.DELETED_FIELD)
    );
  }
}
