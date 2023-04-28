package com.sourcefuse.jarc.services.auditservice.audit.softdelete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sourcefuse.jarc.services.auditservice.models.BaseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SoftDeletesRepositoryImpl<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements SoftDeletesRepository<T, ID> {

	private final JpaEntityInformation<T, ?> entityInformation;
	private static final String DELETED_FIELD = "deleted";

	public SoftDeletesRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
	}

	public SoftDeletesRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
	}

	public Iterable<T> findAllActive() {
		return super.findAll(notDeleted());
	}

	@Override
	public Iterable<T> findAllActive(Sort sort) {
		return super.findAll(notDeleted(), sort);
	}

	@Override
	public Page<T> findAllActive(Pageable pageable) {
		return super.findAll(notDeleted(), pageable);
	}

	@Override
	public Iterable<T> findAllActive(Iterable<ID> ids) {
		if (ids == null || !ids.iterator().hasNext())
			return Collections.emptyList();

		if (entityInformation.hasCompositeId()) {

			List<T> results = new ArrayList<T>();

			for (ID id : ids) {
				T entity = findOneActive(id).orElse(null);
				if (entity == null)
					results.add(entity);
			}

			return results;
		}

		ByIdsSpecification<T> specification = new ByIdsSpecification<T>(entityInformation);
		TypedQuery<T> query = getQuery(Specification.where(specification).and(notDeleted()), (Sort) null);

		return query.setParameter(specification.parameter, ids).getResultList();
	}

	@Override
	public Optional<T> findOneActive(ID id) {
		return super.findOne(
				Specification.where(new ByIdSpecification<T, ID>(entityInformation, id)).and(notDeleted()));
	}

	@Override
	@Transactional
	public void softDelete(Iterable<? extends T> entities) {
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		for (T entity : entities)
			softDelete(entity);
	}

	@Override
	@Transactional
	public void softDeleteAll() {
		for (T entity : findAllActive())
			softDelete(entity);
	}

//	@Override
//	@Transactional
//	public void scheduleSoftDelete(ID id) {
//		softDelete(id);
//	}
//
//	@Override
//	@Transactional
//	public void scheduleSoftDelete(T entity) {
//		softDelete(entity);
//	}

	@Override
	@Transactional
	public void softDelete(ID id) {
		Assert.notNull(id, "The given id must not be null!");

		T entity = findOneActive(id).orElse(null);

		if (entity == null)
			throw new EmptyResultDataAccessException(
					String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);

		softDelete(entity);
	}

	@Override
	@Transactional
	public void softDelete(T entity) {
		Assert.notNull(entity, "The entity must not be null!");
		entity.setDeleted(true);
		super.save(entity);
	}

	public long countActive() {
		return super.count(notDeleted());
	}

	@Override
	public boolean existsActive(ID id) {
		Assert.notNull(id, "The entity must not be null!");
		return findOneActive(id) != null ? true : false;
	}

	private static final class ByIdSpecification<T, ID> implements Specification<T> {

		private static final long serialVersionUID = 1L;
		private final JpaEntityInformation<T, ?> entityInformation;
		private final ID id;

		public ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, ID id) {
			this.entityInformation = entityInformation;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()), id);
		}
	}

	@SuppressWarnings("rawtypes")
	private static final class ByIdsSpecification<T> implements Specification<T> {

		private static final long serialVersionUID = 1L;

		private final JpaEntityInformation<T, ?> entityInformation;

		ParameterExpression<Iterable> parameter;

		public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
			this.entityInformation = entityInformation;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = cb.parameter(Iterable.class);
			return path.in(parameter);
		}
	}

	/*
	 * Specification to check if the DELETED_FIELD is null
	 */
	private static final class IsNotDeleted<T> implements Specification<T> {
		private static final long serialVersionUID = 1L;

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.isFalse(root.<Boolean>get(DELETED_FIELD));
		}
	}

	/*
	 * Combined Specification from DeletedIsNull and DeletedTimeGreatherThanNow to
	 * check if the entity is soft deleted or not
	 */
	private static final <T> Specification<T> notDeleted() {
		return Specification.where(new IsNotDeleted<T>());
	}

}