package com.sourcefuse.jarc.core.repositories;

import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

@NoRepositoryBean
public interface SoftDeletesRepository<
    T extends SoftDeleteEntity, TYPE extends Serializable
>
    extends JpaRepository<T, TYPE> {
    void deleteByIdHard(TYPE id);

    void deleteHard(T entity);

    void deleteAllByIdHard(Iterable<? extends TYPE> ids);

    void deleteAllByIdInBatchHard(Iterable<TYPE> ids);

    void deleteAllHard(Iterable<? extends T> entities);

    void deleteAllInBatchHard(Iterable<T> entities);

    void deleteAllHard();

    void deleteAllInBatchHard();

    Optional<T> findByIdIncludeSoftDelete(TYPE id);

    boolean existsByIdIncludeSoftDelete(TYPE id);

    List<T> findAllIncludeSoftDelete();

    List<T> findAllByIdIncludeSoftDelete(Iterable<TYPE> ids);

    List<T> findAllIncludeSoftDelete(Sort sort);

    Page<T> findAllIncludeSoftDelete(Pageable pageable);

    Optional<T> findOneIncludeSoftDelete(Specification<T> spec);

    List<T> findAllIncludeSoftDelete(Specification<T> spec);

    Page<T> findAllIncludeSoftDelete(Specification<T> spec, Pageable pageable);

    List<T> findAllIncludeSoftDelete(Specification<T> spec, Sort sort);

    <S extends T> Optional<S> findOneIncludeSoftDelete(Example<S> example);

    <S extends T> long countIncludeSoftDelete(Example<S> example);

    <S extends T> boolean existsIncludeSoftDelete(Example<S> example);

    boolean existsIncludeSoftDelete(Specification<T> spec);

    long deleteHard(Specification<T> spec);

    <S extends T> List<S> findAllIncludeSoftDelete(Example<S> example);

    <S extends T> List<S> findAllIncludeSoftDelete(
        Example<S> example,
        Sort sort
    );

    <S extends T> Page<S> findAllIncludeSoftDelete(
        Example<S> example,
        Pageable pageable
    );

    <S extends T, R> R findByIncludeSoftDelete(
        Example<S> example,
        Function<FetchableFluentQuery<S>, R> queryFunction
    );

    <S extends T, R> R findByIncludeSoftDelete(
        Specification<T> spec,
        Function<FetchableFluentQuery<S>, R> queryFunction
    );

    long countIncludeSoftDelete();

    long countIncludeSoftDelete(Specification<T> spec);
}