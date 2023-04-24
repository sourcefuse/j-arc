package com.sourcefuse.jarc.services.auditservice.audit.mixin;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;

import com.google.gson.Gson;
import com.sourcefuse.jarc.services.auditservice.constants.Constants.AuditActions;
import com.sourcefuse.jarc.services.auditservice.models.AuditLog;
import com.sourcefuse.jarc.services.auditservice.models.BaseModel;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuditLogService {
	@Async
	public static <T extends BaseModel, ID> void saveAuditLog(EntityManager em, AuditActions action, String before,
			T entity) {
		try {
			em.getTransaction().begin();
			log.info("::::::: {} :::::: ", Thread.currentThread().getName());
			AuditLog auditLog = new AuditLog();
			auditLog.setAction(action);
			auditLog.setActedAt(entity.getTableName());
			auditLog.setActionKey(entity.getClass().getSimpleName() + "_Logs");
			auditLog.setBefore(before);
			auditLog.setAfter(action.toString().contains("DELETE") ? null : new Gson().toJson(entity));
			auditLog.setEntityId(entity.getId());
			auditLog.setActor("");
			log.info("::: audit Log {}", auditLog.toString());
			em.persist(auditLog);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			log.error("::: Something went wrong while saving the audit logs {}", e);

		}
	}

	@Async
	public static <T extends BaseModel, ID> void saveAuditLogs(EntityManager em, AuditActions action,
			Map<ID, String> userMap, Iterable<T> entities) {
		try {
			em.getTransaction().begin();
			if (action.toString().contains("DELETE")) {
				for (Entry<ID, String> entity : userMap.entrySet()) {
					AuditLog auditLog = new AuditLog();
					auditLog.setAction(action);
					auditLog.setActedAt(entities.iterator().next().getTableName());
					auditLog.setActionKey(entities.iterator().next().getClass().getSimpleName() + "_Logs");
					auditLog.setBefore(entity.getValue() != null ? entity.getValue() : null);
					auditLog.setAfter(null);
					auditLog.setEntityId((UUID) entity.getKey());
					auditLog.setActor("");
					em.persist(auditLog);
				}
			} else {
				for (T entity : entities) {
					AuditLog auditLog = new AuditLog();
					auditLog.setAction(action);
					auditLog.setActedAt(entity.getTableName());
					auditLog.setActionKey(entity.getClass().getSimpleName() + "_Logs");
					auditLog.setBefore(userMap.get(entity.getId()) != null ? userMap.get(entity.getId()) : null);
					auditLog.setAfter(new Gson().toJson(entity));
					auditLog.setEntityId(entity.getId());
					auditLog.setActor("");
					em.persist(auditLog);
				}
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("::: Something went wrong while saving the audit logs {}", e);
		}
	}
}
