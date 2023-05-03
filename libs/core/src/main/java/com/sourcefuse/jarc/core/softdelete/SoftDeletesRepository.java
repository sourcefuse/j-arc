package com.sourcefuse.jarc.core.softdelete;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;

import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;

@NoRepositoryBean
public interface SoftDeletesRepository<T extends SoftDeleteEntity, ID extends Serializable> extends JpaRepository<T, ID> {

	List<T> findAllActive();

	List<T> findAllActive(Sort sort);

	Page<T> findAllActive(Pageable pageable);

	List<T> findAllActive(Iterable<ID> ids);

	Optional<T> findActiveById(ID id);

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