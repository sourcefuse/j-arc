package com.sourcefuse.jarc.services.auditservice.audit.mixin;

import java.util.Map;

import com.sourcefuse.jarc.services.auditservice.constants.Constants.AuditActions;
import com.sourcefuse.jarc.services.auditservice.models.BaseEntity;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditLogThread<T extends BaseEntity, ID> extends Thread {
//implements ApplicationContextAware{
	private EntityManager em;
	private AuditActions action;
	private String before;
	private T entity;
	private Map<ID, String> userMap;
	private Iterable<T> entities;


	public AuditLogThread(EntityManager em, AuditActions action, String before, T entity) {
		this.em = em.getEntityManagerFactory().createEntityManager();
		this.action = action;
		this.before = before;
		this.entity = entity;
	}

	public AuditLogThread(EntityManager em, AuditActions action, Map<ID, String> userMap, Iterable<T> entities) {
		this.em = em.getEntityManagerFactory().createEntityManager();
		this.action = action;
		this.userMap = userMap;
		this.entities = entities;
	}

	@Override
	public void run() {
		log.info("Thread started: " + Thread.currentThread().getName());
		try {
			if (entity != null) {
				AuditLogService.saveAuditLog(em, action, before, entity);
			} else if (entities != null) {
				AuditLogService.saveAuditLogs(em, action, userMap, entities);
			} else {
				// do nothing
			}
		} catch (Exception e) {
			log.error("::: Something went wrong while saving the audit logs {}", e);
		}
		log.info("Thread ended: " + Thread.currentThread().getName());
	}
}
