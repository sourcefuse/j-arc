package com.sourcefuse.jarc.core.entitylisteners;

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
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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

  @SuppressWarnings("unchecked")
  private void saveAuditLog(AuditActions action, T entity) {
    EntityManager em = applicationAwareBeanUtils.getNewEntityManager();
    try {
      T before = null;
      T after = entity;

      if (!action.toString().contains("SAVE")) {
        before = (T) em.find(entity.getClass(), entity.getId());
      }
      if (action.toString().contains("DELETE")) {
        after = null;
      }
      em.getTransaction().begin();
      Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();
      UUID actor = authentication != null
        ? ((CurrentUser<?>) authentication.getPrincipal()).getUser().getId()
        : null;
      AuditLog auditLog = new AuditLog();
      auditLog.setAction(action);
      auditLog.setActedOn(entity.getTableName());
      auditLog.setActionKey(entity.getClass().getSimpleName() + "_Logs");
      auditLog.setBefore(before);
      auditLog.setAfter(after);
      auditLog.setEntityId(entity.getId());
      auditLog.setActor(actor);
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
