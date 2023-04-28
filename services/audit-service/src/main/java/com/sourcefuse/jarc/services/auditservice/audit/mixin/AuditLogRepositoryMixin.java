package com.sourcefuse.jarc.services.auditservice.audit.mixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.sourcefuse.jarc.services.auditservice.constants.Constants.AuditActions;
import com.sourcefuse.jarc.services.auditservice.models.BaseEntity;

import jakarta.persistence.EntityManager;

public class AuditLogRepositoryMixin<T extends BaseEntity, ID> extends SimpleJpaRepository<T, ID>
		implements AuditLogRepository<T, ID> {

	private EntityManager em;
	
	AuditLogRepositoryMixin(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
	}

	public AuditLogRepositoryMixin(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public <S extends T> S save(S entity) {
		T existing = entity.getId() != null ? super.findById((ID) entity.getId()).orElse(null) : null;
		String existingUserGson = existing != null ? new Gson().toJson(existing) : null;
		S created = super.save(entity);

		new AuditLogThread<S, ID>(em, AuditActions.SAVE, existingUserGson, created).start();
		return created;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public <S extends T> S saveAndFlush(S entity) {
		T existing = entity.getId() != null ? super.findById((ID) entity.getId()).orElse(null) : null;
		String existingUserGson = existing != null ? new Gson().toJson(existing) : null;
		S created = super.saveAndFlush(entity);

		new AuditLogThread<S, ID>(em, AuditActions.SAVE_AND_FLUSH, existingUserGson, created).start();
		;
		return created;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		Set<ID> ids = new HashSet<>();
		for (S entity : entities) {
			if (entity.getId() != null) {
				ids.add((ID) entity.getId());
			}

		}
		List<T> existingEntities = super.findAllById(ids);
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : existingEntities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}
		List<S> created = new ArrayList<>();
		for (S entity : entities) {
			created.add(super.save(entity));
		}

		new AuditLogThread<S, ID>(em, AuditActions.SAVE_ALL, userMap, created).start();
		return created;

	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
		Set<ID> ids = new HashSet<>();
		for (S entity : entities) {
			if (entity.getId() != null) {
				ids.add((ID) entity.getId());
			}

		}
		List<T> existingEntities = super.findAllById(ids);
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : existingEntities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}
		List<S> created = new ArrayList<>();
		for (S entity : entities) {
			created.add(super.saveAndFlush(entity));
		}

		new AuditLogThread<S, ID>(em, AuditActions.SAVE_ALL, userMap, created).start();
		return created;

	}

	@Override
	@Transactional
	public void deleteById(ID id) {
		T existing = super.findById(id).orElse(null);
		String existingUserGson = existing != null ? new Gson().toJson(existing) : null;
		super.deleteById(id);

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_BY_ID, existingUserGson, existing).start();
	}

	@Override
	@Transactional
	public void delete(T entity) {

		String existingUserGson = entity != null ? new Gson().toJson(entity) : null;
		super.delete(entity);

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_BY_ENTITY, existingUserGson, entity).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void deleteAllById(Iterable<? extends ID> ids) {

		List<T> existingEntities = super.findAllById((Iterable<ID>) ids);
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : existingEntities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}
		super.deleteAllById(ids);

//		for (ID id : ids) {
//			AuditLog auditLog = new AuditLog();
//			auditLog.setAction("DeleteAllById");
//			auditLog.setBefore(userMap.get(id) != null ? userMap.get(id) : null);
//			auditLog.setAfter(null);
//			em.persist(auditLog);
//		}

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_ALL_BY_ID, userMap, existingEntities).start();

	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void deleteAllByIdInBatch(Iterable<ID> ids) {

		List<T> existingEntities = super.findAllById((Iterable<ID>) ids);
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : existingEntities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}
		super.deleteAllByIdInBatch(ids);

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_ALL_BY_ID_IN_BATCH, userMap, existingEntities).start();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void deleteAll(Iterable<? extends T> entities) {

		Map<ID, String> userMap = new HashMap<>();
		List<T> entitiesCopy = new ArrayList<T>();
		for (T existingEntity : entities) {
			entitiesCopy.add(existingEntity);
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}

		for (T entity : entities) {
			delete(entity);
		}

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_ALL_BY_ENTITY, userMap, entitiesCopy).start();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void deleteAllInBatch(Iterable<T> entities) {
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : entities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}

		super.deleteAllInBatch(entities);

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_ALL_IN_BATCH, userMap, entities).start();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void deleteAll() {

		List<T> entities = findAll();
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : entities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}

		for (T entity : entities) {
			super.delete(entity);
		}

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_ALL, userMap, entities).start();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void deleteAllInBatch() {

		List<T> entities = findAll();
		Map<ID, String> userMap = new HashMap<>();
		for (T existingEntity : entities) {
			userMap.put((ID) existingEntity.getId(), new Gson().toJson(existingEntity));
		}

		super.deleteAllInBatch();

		new AuditLogThread<T, ID>(em, AuditActions.DELETE_ALL_IN_BATCH, userMap, entities).start();
	}
	
//	@Transactional
//	@SuppressWarnings("unchecked")
//	public void softDeleteById(ID id) throws Exception {
//
//		T entity = findById(id).orElse(null);
//		if(entity == null) {
//			throw new Exception("Entity for given id not found");
//		}
//		entity.setDeleted(true);
//		super.deleteAllInBatch();
//		
//		super.save(entity);
//	}
}