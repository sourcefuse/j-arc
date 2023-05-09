package com.sourcefuse.jarc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.sourcefuse.jarc.core.constants.TestConstants;
import com.sourcefuse.jarc.core.models.audit.AuditLog;
import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepositoryImpl;
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
public class AuditLogEntityListenerNegativeTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  RoleRepository roleRepository;

  @BeforeEach
  void clearUserAndAuditLog() {
    TestConstants.clearTables(entityManager);
  }

  /**
   * should not save AuditLog when Role is saved since authentication is not
   * exists
   */
  @Test
  void shouldNotSaveAuditLogWhenRoleIsSaved() {
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

    assertEquals(0, auditLogs.size());
  }

  /**
   * should not save AuditLog when Role is updated since authentication is not
   * exists
   */
  @Test
  void shouldNotSaveAuditLogWhenRoleIsUpdated() {
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

    assertEquals(0, auditLogs.size());
  }

  /**
   * should not save AuditLog when Role is hard deleted since authentication is
   * not exists
   */
  @Test
  void shouldNotSaveAuditLogWhenRoleIsHardDeleted() {
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

    assertEquals(0, auditLogs.size());
  }

  /**
   * should not save AuditLog when Role is soft deleted since authentication is
   * not exists
   */
  @Test
  void shouldSaveAuditLogWhenRoleIsSoftDeleted() {
    Role role = new Role();
    role.setName("ABC");
    role.setPermissions("XYZ");
    role = this.roleRepository.save(role);
    List<Role> roles = this.roleRepository.findAll();

    assertEquals(1, roles.size());
    try {
      this.roleRepository.deleteById(role.getId());
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Forbidden :: User is not Authenticated");
    }

    roles = this.roleRepository.findAll();
    assertEquals(roles.size(), 1);

    List<AuditLog> auditLogs = entityManager
      .createQuery(
        "Select a from AuditLog a order by a.actedOn desc",
        AuditLog.class
      )
      .getResultList();

    assertEquals(0, auditLogs.size());
  }
}
