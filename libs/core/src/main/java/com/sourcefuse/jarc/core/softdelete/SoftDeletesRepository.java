package com.sourcefuse.jarc.core.softdelete;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;

import com.sourcefuse.jarc.core.models.base.BaseEntity;

@NoRepositoryBean
public interface SoftDeletesRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {

	Iterable<T> findAllActive();

	Iterable<T> findAllActive(Sort sort);

	Page<T> findAllActive(Pageable pageable);

	Iterable<T> findAllActive(Iterable<ID> ids);

	Optional<T> findOneActive(ID id);

	@Modifying
	void softDeleteById(ID id);

	@Modifying
	void softDelete(T entity);

	@Modifying
	void softDelete(Iterable<? extends T> entities);

	@Modifying
	void softDeleteAll();

//	@Modifying
//	void scheduleSoftDelete(ID id);
//
//	@Modifying
//	void scheduleSoftDelete(T entity);

	long countActive();

	boolean existsActive(ID id);

}