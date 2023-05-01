package com.sourcefuse.jarc.services.auditservice.audit.entitylistener;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.Gson;
import com.sourcefuse.jarc.services.auditservice.constants.Constants.AuditActions;
import com.sourcefuse.jarc.services.auditservice.models.BaseEntity;
import com.sourcefuse.jarc.services.auditservice.audit.models.AuditLog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import com.sourcefuse.jarc.services.authservice.session.CurrentUser;

@Slf4j
public class AuditLogEntityListener<T extends BaseEntity> {

	@PostPersist
	public void onPersist(T target) {
		this.saveAuditLog(AuditActions.SAVE, target);
	}

	@PostUpdate
	public void onUpdate(T target) {
		if (!target.isDeleted()) {
			this.saveAuditLog(AuditActions.UPDATE, target);
		} else {
			this.saveAuditLog(AuditActions.DELETE, target);
		}
	}

	@PostRemove
	public void onRemove(T target) {
		this.saveAuditLog(AuditActions.DELETE, target);
	}

	private EntityManager getEntityManager() throws Exception {
		EntityManager em = BeanUtils.getBean(EntityManager.class);
		if (em == null) {
			throw new Exception("Entity Manager is null can not proceed to save audit log");
		}
		return em.getEntityManagerFactory().createEntityManager();
	}

	@SuppressWarnings("unchecked")
	private void saveAuditLog(AuditActions action, T entity) {
		try {
			String before = null;
			String after = new Gson().toJson(entity);
			EntityManager em = this.getEntityManager();
			if (!action.toString().contains("SAVE")) {
				T oldEntity = (T) em.find(entity.getClass(), entity.getId());
				before = oldEntity != null ? new Gson().toJson(oldEntity) : null;
			}
			if (action.toString().contains("DELETE")) {
				after = null;
			}
			em.getTransaction().begin();
			CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			AuditLog auditLog = new AuditLog();
			auditLog.setAction(action);
			auditLog.setActedAt(entity.getTableName());
			auditLog.setActionKey(entity.getClass().getSimpleName() + "_Logs");
			auditLog.setBefore(before);
			auditLog.setAfter(after);
			auditLog.setEntityId(entity.getId());
			auditLog.setActor(currentUser.getUser().getId());
			log.info("::: audit Log {}", auditLog.toString());
			em.persist(auditLog);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			log.error("::: Something went wrong while saving the audit logs {}", e);
		} finally {
		}

	}
}
