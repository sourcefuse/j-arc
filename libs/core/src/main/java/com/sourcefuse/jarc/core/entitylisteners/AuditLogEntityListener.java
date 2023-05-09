package com.sourcefuse.jarc.core.entitylisteners;

import com.google.gson.Gson;
import com.sourcefuse.jarc.core.awares.ApplicationAwareBeanUtils;
import com.sourcefuse.jarc.core.constants.AuditActions;
import com.sourcefuse.jarc.core.models.audit.AuditLog;
import com.sourcefuse.jarc.core.models.base.BaseEntity;
import com.sourcefuse.jarc.core.models.base.SoftDeleteEntity;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuditLogEntityListener<T extends BaseEntity> {

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
    try {
      EntityManager em = ApplicationAwareBeanUtils.getNewEntityManager();
      this.saveLogInDb(em, action, entity);
    } catch (IllegalArgumentException e) {
      log.error("Something went wrong while saving the audit logs {}", e);
    }
  }

  private void saveLogInDb(EntityManager em, AuditActions action, T entity) {
    try {
      String before = null;
      String after = new Gson().toJson(entity);
      if (!action.toString().contains("SAVE")) {
        @SuppressWarnings("unchecked")
        T oldEntity = (T) em.find(entity.getClass(), entity.getId());
        before = oldEntity != null ? new Gson().toJson(oldEntity) : null;
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
