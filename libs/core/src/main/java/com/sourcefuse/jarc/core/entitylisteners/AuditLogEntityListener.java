package com.sourcefuse.jarc.core.entitylisteners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourcefuse.jarc.core.adapters.LocalDateTimeTypeAdapter;
import com.sourcefuse.jarc.core.awares.ApplicationAwareBeanUtils;
import com.sourcefuse.jarc.core.enums.AuditActions;
import com.sourcefuse.jarc.core.models.audit.AuditLog;
import com.sourcefuse.jarc.core.models.base.BaseEntity;
import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class AuditLogEntityListener<T extends BaseEntity> {

  ApplicationAwareBeanUtils applicationAwareBeanUtils;

  @PostPersist
  public void onPersist(T target) {
    this.saveAuditLog(AuditActions.SAVE, target);
  }

  @PostUpdate
  public void onUpdate(T target) {
    if (target instanceof SoftDeleteEntity softDeletedEntity) {
      if (!softDeletedEntity.isDeleted()) {
        this.saveAuditLog(AuditActions.UPDATE, target);
      } else {
        this.saveAuditLog(AuditActions.DELETE, target);
      }
    } else {
      this.saveAuditLog(AuditActions.UPDATE, target);
    }
  }

	@PostRemove
	public void onRemove(T target) {
		this.saveAuditLog(AuditActions.DELETE, target);
	}

  private void saveAuditLog(AuditActions action, T entity) {
    EntityManager em = applicationAwareBeanUtils.getNewEntityManager();
    try {
      Gson gson = new GsonBuilder()
        .registerTypeAdapter(
          LocalDateTime.class,
          new LocalDateTimeTypeAdapter()
        )
        .create();
      String before = null;
      String after = gson.toJson(entity);
      if (!action.toString().contains("SAVE")) {
        @SuppressWarnings("unchecked")
        T oldEntity = (T) em.find(entity.getClass(), entity.getId());
        before = oldEntity != null ? gson.toJson(oldEntity) : null;
      }
      if (action.toString().contains("DELETE")) {
        after = null;
      }
      em.getTransaction().begin();
      CurrentUser<?> currentUser = (CurrentUser<?>) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
      AuditLog auditLog = new AuditLog();
      auditLog.setAction(action);
      auditLog.setActedOn(entity.getTableName());
      auditLog.setActionKey(entity.getClass().getSimpleName() + "_Logs");
      auditLog.setBefore(before);
      auditLog.setAfter(after);
      auditLog.setEntityId(entity.getId());
      auditLog.setActor(currentUser.getUser().getId());
      log.info("audit Log {}", auditLog.toString());
      em.persist(auditLog);
      em.getTransaction().commit();
    } catch (Exception ex) {
      log.error("Something went wrong while saving the audit logs {}", ex);
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }
}
