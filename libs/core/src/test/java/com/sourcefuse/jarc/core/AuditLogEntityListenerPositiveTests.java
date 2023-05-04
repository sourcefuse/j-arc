package com.sourcefuse.jarc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sourcefuse.jarc.core.constants.Constants.AuditActions;
import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.models.audit.AuditLog;
import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.test.models.Role;
import com.sourcefuse.jarc.core.test.repositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan({ "com.sourcefuse.jarc.core" })
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class AuditLogEntityListenerPositiveTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  RoleRepository roleRepository;

  @BeforeEach
  void clearUserAndAuditLog() {
    TestConstants.clearTables(entityManager);
    TestConstants.setCurrentLoggedInUser();
  }

  @AfterEach
  public void waitAfterTest() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(500);
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsSaved() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissons("XYZ");
    role = this.roleRepository.save(role);
    List<Role> roles = this.roleRepository.findAll();
    assertEquals(roles.size(), 1);
    assertEquals(role.getId(), roles.get(0).getId());
    assertEquals(role.getName(), roles.get(0).getName());
    assertEquals(role.getPermissons(), roles.get(0).getPermissons());

    List<AuditLog> auditLogs = entityManager
      .createQuery("Select a from AuditLog a", AuditLog.class)
      .getResultList();

    assertEquals(auditLogs.size(), 1);
    assertEquals(auditLogs.get(0).getAction(), AuditActions.SAVE);
    assertEquals(auditLogs.get(0).getEntityId(), role.getId());
    assertEquals(auditLogs.get(0).getActedOn(), role.getTableName());
    assertEquals(auditLogs.get(0).getActionKey(), "Role_Logs");
    assertNull(auditLogs.get(0).getBefore());
    assertNotNull(auditLogs.get(0).getAfter());
    assertEquals(TestConstants.mockUserId, auditLogs.get(0).getActor());
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsUpdated() {
    String oldName = "oldName", updatedName = "updatedName";
    Role role = new Role();
    role.setName(oldName);
    role.setPermissons("XYZ");
    role = this.roleRepository.save(role);

    role.setName(updatedName);
    role = this.roleRepository.save(role);

    List<Role> roles = this.roleRepository.findAll();
    assertEquals(roles.size(), 1);
    assertEquals(role.getId(), roles.get(0).getId());
    assertEquals(role.getName(), roles.get(0).getName());
    assertEquals(updatedName, roles.get(0).getName());
    assertNotEquals(oldName, roles.get(0).getName());
    assertEquals(role.getPermissons(), roles.get(0).getPermissons());

    List<AuditLog> auditLogs = entityManager
      .createQuery(
        "Select a from AuditLog a order by a.actedOn desc",
        AuditLog.class
      )
      .getResultList();

    assertEquals(auditLogs.size(), 2);

    AuditLog saveRoleauditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.SAVE))
      .findFirst()
      .orElse(null);
    assertNotNull(saveRoleauditLog);

    AuditLog updateRoleauditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.UPDATE))
      .findFirst()
      .orElse(null);
    assertNotNull(updateRoleauditLog);

    assertEquals(updateRoleauditLog.getEntityId(), role.getId());
    assertEquals(updateRoleauditLog.getActedOn(), role.getTableName());
    assertEquals(updateRoleauditLog.getActionKey(), "Role_Logs");
    assertNotNull(updateRoleauditLog.getBefore());
    assertNotNull(updateRoleauditLog.getAfter());
    assertEquals(TestConstants.mockUserId, updateRoleauditLog.getActor());
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsDeleted() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissons("XYZ");
    role = this.roleRepository.save(role);
    this.roleRepository.deleteById(role.getId());

    List<Role> roles = this.roleRepository.findAll();
    assertEquals(roles.size(), 0);

    List<AuditLog> auditLogs = entityManager
      .createQuery(
        "Select a from AuditLog a order by a.actedOn desc",
        AuditLog.class
      )
      .getResultList();

    assertEquals(auditLogs.size(), 2);

    AuditLog saveRoleauditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.SAVE))
      .findFirst()
      .orElse(null);
    assertNotNull(saveRoleauditLog);

    AuditLog deleteRoleauditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.DELETE))
      .findFirst()
      .orElse(null);
    assertNotNull(deleteRoleauditLog);

    assertEquals(deleteRoleauditLog.getEntityId(), role.getId());
    assertEquals(deleteRoleauditLog.getActedOn(), role.getTableName());
    assertEquals(deleteRoleauditLog.getActionKey(), "Role_Logs");
    assertNull(deleteRoleauditLog.getAfter());
    assertNotNull(deleteRoleauditLog.getBefore());
    assertEquals(TestConstants.mockUserId, deleteRoleauditLog.getActor());
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsSoftDeleted() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissons("XYZ");
    role = this.roleRepository.save(role);
    this.roleRepository.softDeleteById(role.getId());

    List<Role> roles = this.roleRepository.findAll();
    assertEquals(roles.size(), 1);
    assertEquals(roles.get(0).isDeleted(), true);
    assertNotNull(roles.get(0).getDeletedOn());
    assertNotNull(roles.get(0).getDeletedBy());

    List<AuditLog> auditLogs = entityManager
      .createQuery(
        "Select a from AuditLog a order by a.actedOn desc",
        AuditLog.class
      )
      .getResultList();

    assertEquals(auditLogs.size(), 2);

    AuditLog saveRoleauditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.SAVE))
      .findFirst()
      .orElse(null);
    assertNotNull(saveRoleauditLog);

    AuditLog deletedRoleauditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.DELETE))
      .findFirst()
      .orElse(null);
    assertNotNull(deletedRoleauditLog);

    assertEquals(deletedRoleauditLog.getEntityId(), role.getId());
    assertEquals(deletedRoleauditLog.getActedOn(), role.getTableName());
    assertEquals(deletedRoleauditLog.getActionKey(), "Role_Logs");
    assertNull(deletedRoleauditLog.getAfter());
    assertNotNull(deletedRoleauditLog.getBefore());
    assertEquals(TestConstants.mockUserId, deletedRoleauditLog.getActor());
  }
}
