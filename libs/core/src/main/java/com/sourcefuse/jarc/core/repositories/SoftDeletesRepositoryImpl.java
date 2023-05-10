package com.sourcefuse.jarc.core.repositories;

import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_BY_ID_STRING;

import com.sourcefuse.jarc.core.constants.SoftDeleteRepositoryConstants;
import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import com.sourcefuse.jarc.core.repositories.specifications.ByIdSpecification;
import com.sourcefuse.jarc.core.repositories.specifications.IsNotDeleted;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class SoftDeletesRepositoryImpl<
  T extends SoftDeleteEntity, ID extends Serializable
>
  extends SimpleJpaRepository<T, ID>
  implements SoftDeletesRepository<T, ID> {

  private final PersistenceProvider provider;

  @Nullable
  private CrudMethodMetadata metadata;

  private final JpaEntityInformation<T, ?> entityInformation;
  EntityManager em;

  public SoftDeletesRepositoryImpl(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
    this.em = em;
    this.entityInformation =
      JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    this.provider = PersistenceProvider.fromEntityManager(em);
  }

  public SoftDeletesRepositoryImpl(
    JpaEntityInformation<T, ?> entityInformation,
    EntityManager entityManager
  ) {
    super(entityInformation, entityManager);
    this.em = entityManager;
    this.entityInformation = entityInformation;
    this.provider = PersistenceProvider.fromEntityManager(entityManager);
  }

  @Override
  public void setRepositoryMethodMetadata(
    CrudMethodMetadata crudMethodMetadata
  ) {
    this.metadata = crudMethodMetadata;
  }

  @Nullable
  @Override
  protected CrudMethodMetadata getRepositoryMethodMetadata() {
    return metadata;
  }

  @Override
  protected Class<T> getDomainClass() {
    return entityInformation.getJavaType();
  }

  private static final <T> Specification<T> notDeleted() {
    return Specification.where(new IsNotDeleted<T>());
  }

  @SuppressWarnings("rawtypes")
  private static final class ByIdsSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = -3244704710376973492L;

    private final JpaEntityInformation<T, ?> entityInformation;

    ParameterExpression<Iterable> parameter;

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

  // INFO: overridden soft delete and
  // find non soft deleted entries method
  @Override
  @Transactional
  public void deleteById(ID id) {
    Assert.notNull(id, SoftDeleteRepositoryConstants.ID_MUST_NOT_BE_NULL);
    T entity = findById(id).orElse(null);
    if (entity == null) {
      throw new EmptyResultDataAccessException(
        String.format(
          "No %s entity with id %s exists!",
          entityInformation.getJavaType(),
          id
        ),
        1
      );
    }
    delete(entity);
  }

  @Override
  @Transactional
  public void delete(T entity) {
    Assert.notNull(
      entity,
      SoftDeleteRepositoryConstants.ENTITY_MUST_NOT_BE_NULL
    );
    entity.setDeleted(true);
    super.save(entity);
  }

  @Override
  @Transactional
  public void deleteAllById(Iterable<? extends ID> ids) {
    Assert.notNull(ids, SoftDeleteRepositoryConstants.IDS_MUST_NOT_BE_NULL);
    for (ID id : ids) {
      deleteById(id);
    }
  }

  @Override
  @Transactional
  public void deleteAllByIdInBatch(Iterable<ID> ids) {
    Assert.notNull(ids, SoftDeleteRepositoryConstants.IDS_MUST_NOT_BE_NULL);
    deleteAllInBatch(findAllById(ids));
  }

  @Override
  @Transactional
  public void deleteAll(Iterable<? extends T> entities) {
    Assert.notNull(
      entities,
      SoftDeleteRepositoryConstants.ENTITIES_MUST_NOT_BE_NULL
    );
    for (T entity : entities) {
      entity.setDeleted(true);
      super.save(entity);
    }
  }

  @Override
  @Transactional
  public void deleteAllInBatch(Iterable<T> entities) {
    Assert.notNull(
      entities,
      SoftDeleteRepositoryConstants.ENTITIES_MUST_NOT_BE_NULL
    );
    for (T entity : entities) {
      entity.setDeleted(true);
    }
    super.saveAll(entities);
  }

  @Override
  @Transactional
  public void deleteAll() {
    deleteAll(findAll());
  }

  @Override
  @Transactional
  public void deleteAllInBatch() {
    deleteAllInBatch(findAll());
  }

  @Override
  @Transactional
  public Optional<T> findById(ID id) {
    Assert.notNull(id, SoftDeleteRepositoryConstants.ID_MUST_NOT_BE_NULL);
    return super.findOne(
      Specification
        .where(new ByIdSpecification<T, ID>(entityInformation, id))
        .and(notDeleted())
    );
  }

  @Override
  @Transactional
  public boolean existsById(ID id) {
    Assert.notNull(id, SoftDeleteRepositoryConstants.ENTITY_MUST_NOT_BE_NULL);
    return findById(id).orElse(null) != null;
  }

  @Override
  @Transactional
  public List<T> findAll() {
    return super.findAll(notDeleted());
  }

  @Override
  @Transactional
  public List<T> findAllById(Iterable<ID> ids) {
    Assert.notNull(ids, SoftDeleteRepositoryConstants.IDS_MUST_NOT_BE_NULL);
    if (!ids.iterator().hasNext()) {
      return Collections.emptyList();
    }
    if (entityInformation.hasCompositeId()) {
      List<T> results = new ArrayList<>();
      for (ID id : ids) {
        T entity = findById(id).orElse(null);
        if (entity == null) {
          results.add(entity);
        }
      }
      return results;
    }
    ByIdsSpecification<T> specification = new ByIdsSpecification<>(
      entityInformation
    );
    TypedQuery<T> query = getQuery(
      Specification.where(specification).and(notDeleted()),
      Sort.by(Sort.Direction.ASC, "id")
    );
    return query.setParameter(specification.parameter, ids).getResultList();
  }

  @Override
  @Transactional
  public List<T> findAll(Sort sort) {
    return super.findAll(notDeleted(), sort);
  }

  @Override
  @Transactional
  public Page<T> findAll(Pageable pageable) {
    return super.findAll(notDeleted(), pageable);
  }

  @Override
  @Transactional
  public Optional<T> findOne(@Nullable Specification<T> spec) {
    return super.findOne(addNotDeletedSpecification(spec));
  }

  @Override
  @Transactional
  public List<T> findAll(@Nullable Specification<T> spec) {
    return super.findAll(addNotDeletedSpecification(spec));
  }

  @Override
  @Transactional
  public Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable) {
    return super.findAll(addNotDeletedSpecification(spec), pageable);
  }

  @Override
  @Transactional
  public List<T> findAll(@Nullable Specification<T> spec, Sort sort) {
    return super.findAll(addNotDeletedSpecification(spec), sort);
  }

  @Override
  @Transactional
  public <S extends T> Optional<S> findOne(Example<S> example) {
    example.getProbe().setDeleted(false);
    return super.findOne(example);
  }

  @Override
  @Transactional
  public <S extends T> long count(Example<S> example) {
    example.getProbe().setDeleted(false);
    return super.count(example);
  }

  @Override
  @Transactional
  public <S extends T> boolean exists(Example<S> example) {
    example.getProbe().setDeleted(false);
    return super.exists(example);
  }

  @Override
  @Transactional
  public boolean exists(Specification<T> spec) {
    return super.exists(addNotDeletedSpecification(spec));
  }

  @Override
  @Transactional
  public long delete(Specification<T> spec) {
    List<T> entities = findAll(spec);
    deleteAllInBatch(entities);
    return entities.size();
  }

  @Override
  @Transactional
  public <S extends T> List<S> findAll(Example<S> example) {
    example.getProbe().setDeleted(false);
    return super.findAll(example);
  }

  @Override
  @Transactional
  public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
    example.getProbe().setDeleted(false);
    return super.findAll(example, sort);
  }

  @Override
  @Transactional
  public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
    example.getProbe().setDeleted(false);
    return super.findAll(example, pageable);
  }

  @Override
  @Transactional
  public <S extends T, R> R findBy(
    Example<S> example,
    Function<FetchableFluentQuery<S>, R> queryFunction
  ) {
    Assert.notNull(example, "Sample must not be null");
    Assert.notNull(queryFunction, "Query function must not be null");
    example.getProbe().setDeleted(false);
    return super.findBy(example, queryFunction);
  }

  @Override
  @Transactional
  public <S extends T, R> R findBy(
    Specification<T> spec,
    Function<FetchableFluentQuery<S>, R> queryFunction
  ) {
    return super.findBy(addNotDeletedSpecification(spec), queryFunction);
  }

  @Override
  @Transactional
  public long count() {
    return super.count(notDeleted());
  }

  @Override
  @Transactional
  public long count(@Nullable Specification<T> spec) {
    return super.count(addNotDeletedSpecification(spec));
  }

  // INFO: Existing methods for hard delete
  // and find all with soft deleted methods

  @Override
  @Transactional
  public void deleteByIdHard(ID id) {
    Assert.notNull(id, SoftDeleteRepositoryConstants.ID_MUST_NOT_BE_NULL);
    findById(id).ifPresent(this::deleteHard);
  }

  @Override
  @Transactional
  public void deleteHard(T entity) {
    Assert.notNull(
      entity,
      SoftDeleteRepositoryConstants.ENTITY_MUST_NOT_BE_NULL
    );
    super.delete(entity);
  }

  @Override
  @Transactional
  public void deleteAllByIdHard(Iterable<? extends ID> ids) {
    Assert.notNull(ids, SoftDeleteRepositoryConstants.IDS_MUST_NOT_BE_NULL);
    for (ID id : ids) {
      deleteByIdHard(id);
    }
  }

  @Override
  @Transactional
  public void deleteAllByIdInBatchHard(Iterable<ID> ids) {
    Assert.notNull(ids, SoftDeleteRepositoryConstants.IDS_MUST_NOT_BE_NULL);
    if (!ids.iterator().hasNext()) {
      return;
    }
    if (entityInformation.hasCompositeId()) {
      List<T> entities = new ArrayList<>();
      ids.forEach(id -> entities.add(getReferenceById(id)));
      deleteAllInBatchHard(entities);
    } else {
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
      String queryString = String.format(
        DELETE_ALL_QUERY_BY_ID_STRING,
        entityInformation.getEntityName(),
        idAttributeName
      );
      Query query = em.createQuery(queryString);
      if (ids instanceof Collection) {
        query.setParameter("ids", ids);
      } else {
        Collection<ID> idsCollection = StreamSupport
          .stream(ids.spliterator(), false)
          .collect(Collectors.toCollection(ArrayList::new));
        query.setParameter("ids", idsCollection);
      }
      checkAndApplyQueryHints(query);
      query.executeUpdate();
    }
  }

  @Override
  @Transactional
  public void deleteAllHard(Iterable<? extends T> entities) {
    Assert.notNull(
      entities,
      SoftDeleteRepositoryConstants.ENTITIES_MUST_NOT_BE_NULL
    );
    for (T entity : entities) {
      deleteHard(entity);
    }
  }

  @Override
  @Transactional
  public void deleteAllInBatchHard(Iterable<T> entities) {
    Assert.notNull(
      entities,
      SoftDeleteRepositoryConstants.ENTITIES_MUST_NOT_BE_NULL
    );
    super.deleteAllInBatch(entities);
  }

  @Override
  @Transactional
  public void deleteAllHard() {
    for (T element : findAllIncludeSoftDelete()) {
      deleteHard(element);
    }
  }

  @Override
  @Transactional
  public void deleteAllInBatchHard() {
    super.deleteAllInBatch();
  }

  @Override
  @Transactional
  public Optional<T> findByIdIncludeSoftDelete(ID id) {
    Assert.notNull(id, SoftDeleteRepositoryConstants.ID_MUST_NOT_BE_NULL);
    return super.findById(id);
  }

  @Override
  @Transactional
  public boolean existsByIdIncludeSoftDelete(ID id) {
    Assert.notNull(id, SoftDeleteRepositoryConstants.ID_MUST_NOT_BE_NULL);
    return super.findById(id).isPresent();
  }

  @Override
  @Transactional
  public List<T> findAllIncludeSoftDelete() {
    return super.findAll();
  }

  @Override
  @Transactional
  public List<T> findAllByIdIncludeSoftDelete(Iterable<ID> ids) {
    if (!ids.iterator().hasNext()) {
      return Collections.emptyList();
    }
    if (entityInformation.hasCompositeId()) {
      List<T> results = new ArrayList<>();
      for (ID id : ids) {
        findByIdIncludeSoftDelete(id).ifPresent(results::add);
      }
      return results;
    }
    Collection<ID> idCollection = Streamable.of(ids).toList();
    ByIdsSpecification<T> specification = new ByIdsSpecification<>(
      entityInformation
    );
    TypedQuery<T> query = getQuery(specification, Sort.unsorted());
    return query
      .setParameter(specification.parameter, idCollection)
      .getResultList();
  }

  @Override
  @Transactional
  public List<T> findAllIncludeSoftDelete(Sort sort) {
    return super.findAll(sort);
  }

  @Override
  @Transactional
  public Page<T> findAllIncludeSoftDelete(Pageable pageable) {
    if (pageable.isUnpaged()) {
      return new PageImpl<>(findAll());
    }
    return super.findAll((Specification<T>) null, pageable);
  }

  @Override
  @Transactional
  public Optional<T> findOneIncludeSoftDelete(@Nullable Specification<T> spec) {
    return super.findOne(spec);
  }

  @Override
  @Transactional
  public List<T> findAllIncludeSoftDelete(@Nullable Specification<T> spec) {
    return super.findAll(spec);
  }

  @Override
  @Transactional
  public Page<T> findAllIncludeSoftDelete(
    @Nullable Specification<T> spec,
    Pageable pageable
  ) {
    return super.findAll(spec, pageable);
  }

  @Override
  @Transactional
  public List<T> findAllIncludeSoftDelete(
    @Nullable Specification<T> spec,
    Sort sort
  ) {
    return super.findAll(spec, sort);
  }

  @Override
  @Transactional
  public <S extends T> Optional<S> findOneIncludeSoftDelete(
    Example<S> example
  ) {
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.findOne(Example.of(example.getProbe(), newMatcher));
  }

  @Override
  @Transactional
  public <S extends T> long countIncludeSoftDelete(Example<S> example) {
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.count(Example.of(example.getProbe(), newMatcher));
  }

  @Override
  @Transactional
  public <S extends T> boolean existsIncludeSoftDelete(Example<S> example) {
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.exists(Example.of(example.getProbe(), newMatcher));
  }

  @Override
  @Transactional
  public boolean existsIncludeSoftDelete(Specification<T> spec) {
    return super.exists(spec);
  }

  @Override
  @Transactional
  public long deleteHard(Specification<T> spec) {
    return super.delete(spec);
  }

  @Override
  @Transactional
  public <S extends T> List<S> findAllIncludeSoftDelete(Example<S> example) {
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.findAll(Example.of(example.getProbe(), newMatcher));
  }

  @Override
  @Transactional
  public <S extends T> List<S> findAllIncludeSoftDelete(
    Example<S> example,
    Sort sort
  ) {
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.findAll(Example.of(example.getProbe(), newMatcher), sort);
  }

  @Override
  @Transactional
  public <S extends T> Page<S> findAllIncludeSoftDelete(
    Example<S> example,
    Pageable pageable
  ) {
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.findAll(Example.of(example.getProbe(), newMatcher), pageable);
  }

  @Override
  @Transactional
  public <S extends T, R> R findByIncludeSoftDelete(
    Example<S> example,
    Function<FetchableFluentQuery<S>, R> queryFunction
  ) {
    Assert.notNull(example, "Sample must not be null");
    Assert.notNull(queryFunction, "Query function must not be null");
    ExampleMatcher newMatcher = example
      .getMatcher()
      .withIgnorePaths(SoftDeleteRepositoryConstants.DELETED_FIELD);
    return super.findBy(
      Example.of(example.getProbe(), newMatcher),
      queryFunction
    );
  }

  @Override
  @Transactional
  public <S extends T, R> R findByIncludeSoftDelete(
    Specification<T> spec,
    Function<FetchableFluentQuery<S>, R> queryFunction
  ) {
    return super.findBy(addNotDeletedSpecification(spec), queryFunction);
  }

  @Override
  @Transactional
  public long countIncludeSoftDelete() {
    return super.count();
  }

  @Override
  @Transactional
  public long countIncludeSoftDelete(@Nullable Specification<T> spec) {
    return super.count(spec);
  }

  private void checkAndApplyQueryHints(Query query) {
    if (metadata == null) {
      return;
    }
    getQueryHints().withFetchGraphs(em).forEach(query::setHint);
    if (provider.getCommentHintKey() != null) {
      query.setHint(
        provider.getCommentHintKey(),
        provider.getCommentHintValue(metadata.getComment())
      );
    }
  }

  private Specification<T> addNotDeletedSpecification(Specification<T> spec) {
    if (spec == null) {
      return notDeleted();
    } else {
      return spec.and(notDeleted());
    }
  }
}
