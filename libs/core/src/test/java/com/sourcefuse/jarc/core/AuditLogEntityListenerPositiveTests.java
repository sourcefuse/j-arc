package com.sourcefuse.jarc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sourcefuse.jarc.core.constants.AuditActions;
import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.models.audit.AuditLog;
import com.sourcefuse.jarc.core.repositories.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.core.test.models.Role;
import com.sourcefuse.jarc.core.test.repositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan({ "com.sourcefuse.jarc.core" })
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
class AuditLogEntityListenerPositiveTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  RoleRepository roleRepository;

  @BeforeEach
  void clearUserAndAuditLog() {
    TestConstants.clearTables(entityManager);
    TestConstants.setCurrentLoggedInUser();
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsSaved() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissions("XYZ");
    role = this.roleRepository.save(role);
    List<Role> roles = this.roleRepository.findAll();
    assertEquals(1, roles.size());
    assertEquals(role.getId(), roles.get(0).getId());
    assertEquals(role.getName(), roles.get(0).getName());
    assertEquals(role.getPermissions(), roles.get(0).getPermissions());

    List<AuditLog> auditLogs = entityManager
      .createQuery("Select a from AuditLog a", AuditLog.class)
      .getResultList();

    assertEquals(1, auditLogs.size());
    assertEquals(AuditActions.SAVE, auditLogs.get(0).getAction());
    assertEquals(role.getId(), auditLogs.get(0).getEntityId());
    assertEquals(role.getTableName(), auditLogs.get(0).getActedOn());
    assertEquals("Role_Logs", auditLogs.get(0).getActionKey());
    assertNull(auditLogs.get(0).getBefore());
    assertNotNull(auditLogs.get(0).getAfter());
    assertEquals(TestConstants.mockUserId, auditLogs.get(0).getActor());
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsUpdated() {
    String oldName = "oldName", updatedName = "updatedName";
    Role role = new Role();
    role.setName(oldName);
    role.setPermissions("XYZ");
    role = this.roleRepository.save(role);

    role.setName(updatedName);
    role = this.roleRepository.save(role);

    List<Role> roles = this.roleRepository.findAll();
    assertEquals(1, roles.size());
    assertEquals(role.getId(), roles.get(0).getId());
    assertEquals(role.getName(), roles.get(0).getName());
    assertEquals(updatedName, roles.get(0).getName());
    assertNotEquals(oldName, roles.get(0).getName());
    assertEquals(role.getPermissions(), roles.get(0).getPermissions());

    List<AuditLog> auditLogs = entityManager
      .createQuery(
        "Select a from AuditLog a order by a.actedOn desc",
        AuditLog.class
      )
      .getResultList();

    assertEquals(auditLogs.size(), 2);

    AuditLog saveRoleAuditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.SAVE))
      .findFirst()
      .orElse(null);
    assertNotNull(saveRoleAuditLog);

    AuditLog updateRoleAuditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.UPDATE))
      .findFirst()
      .orElse(null);
    assertNotNull(updateRoleAuditLog);

    assertEquals(role.getId(), updateRoleAuditLog.getEntityId());
    assertEquals(role.getTableName(), updateRoleAuditLog.getActedOn());
    assertEquals("Role_Logs", updateRoleAuditLog.getActionKey());
    assertNotNull(updateRoleAuditLog.getBefore());
    assertNotNull(updateRoleAuditLog.getAfter());
    assertEquals(TestConstants.mockUserId, updateRoleAuditLog.getActor());
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsHardDeleted() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissions("XYZ");
    role = this.roleRepository.save(role);
    this.roleRepository.deleteByIdHard(role.getId());

    List<Role> roles = this.roleRepository.findAll();
    assertEquals(0, roles.size());

    List<AuditLog> auditLogs = entityManager
      .createQuery(
        "Select a from AuditLog a order by a.actedOn desc",
        AuditLog.class
      )
      .getResultList();

    assertEquals(2, auditLogs.size());

    AuditLog saveRoleAuditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.SAVE))
      .findFirst()
      .orElse(null);
    assertNotNull(saveRoleAuditLog);

    AuditLog deleteRoleAuditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.DELETE))
      .findFirst()
      .orElse(null);
    assertNotNull(deleteRoleAuditLog);

    assertEquals(role.getId(), deleteRoleAuditLog.getEntityId());
    assertEquals(role.getTableName(), deleteRoleAuditLog.getActedOn());
    assertEquals("Role_Logs", deleteRoleAuditLog.getActionKey());
    assertNull(deleteRoleAuditLog.getAfter());
    assertNotNull(deleteRoleAuditLog.getBefore());
    assertEquals(TestConstants.mockUserId, deleteRoleAuditLog.getActor());
  }

  @Test
  void shouldSaveAuditLogWhenRoleIsSoftDeleted() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissions("XYZ");
    role = this.roleRepository.save(role);
    this.roleRepository.deleteById(role.getId());

    List<Role> roles = this.roleRepository.findAllIncludeSoftDelete();
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

    assertEquals(2, auditLogs.size());

    AuditLog saveRoleAuditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.SAVE))
      .findFirst()
      .orElse(null);
    assertNotNull(saveRoleAuditLog);

    AuditLog deletedRoleAuditLog = auditLogs
      .stream()
      .filter(aLog -> aLog.getAction().equals(AuditActions.DELETE))
      .findFirst()
      .orElse(null);
    assertNotNull(deletedRoleAuditLog);

    assertEquals(role.getId(), deletedRoleAuditLog.getEntityId());
    assertEquals(role.getTableName(), deletedRoleAuditLog.getActedOn());
    assertEquals("Role_Logs", deletedRoleAuditLog.getActionKey());
    assertNull(deletedRoleAuditLog.getAfter());
    assertNotNull(deletedRoleAuditLog.getBefore());
    assertEquals(TestConstants.mockUserId, deletedRoleAuditLog.getActor());
  }
}
