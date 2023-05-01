package com.sourcefuse.jarc.services.auditservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.services.auditservice.audit.models.AuditLog;
import com.sourcefuse.jarc.services.auditservice.audit.softdelete.SoftDeletesRepositoryImpl;
import com.sourcefuse.jarc.services.auditservice.test.models.Role;
import com.sourcefuse.jarc.services.auditservice.test.repositories.RoleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@ComponentScan({ "com.sourcefuse.jarc.services.auditservice" })
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

	@AfterEach
	public void waitAfterTest() throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(500);
	}

	/**
	 * should not save AuditLog when Role is saved since authentication is not
	 * exists
	 */
	@Test
	void shouldNotSaveAuditLogWhenRoleIsSaved() {
		Role role = new Role();
		role.setName("ABC");
		role.setPermissons("XYZ");
		role = this.roleRepository.save(role);
		List<Role> roles = this.roleRepository.findAll();
		assertEquals(roles.size(), 1);
		assertEquals(role.getId(), roles.get(0).getId());
		assertEquals(role.getName(), roles.get(0).getName());
		assertEquals(role.getPermissons(), roles.get(0).getPermissons());

		List<AuditLog> auditLogs = entityManager.createQuery("Select a from AuditLog a", AuditLog.class)
				.getResultList();

		assertEquals(auditLogs.size(), 0);
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
				.createQuery("Select a from AuditLog a order by a.actedOn desc", AuditLog.class).getResultList();

		assertEquals(auditLogs.size(), 0);
	}

	/**
	 * should not save AuditLog when Role is deleted since authentication is not
	 * exists
	 */
	@Test
	void shouldNotSaveAuditLogWhenRoleIsDeleted() {
		Role role = new Role();
		role.setName("ABC");
		role.setPermissons("XYZ");
		role = this.roleRepository.save(role);
		this.roleRepository.deleteById(role.getId());

		List<Role> roles = this.roleRepository.findAll();
		assertEquals(roles.size(), 0);

		List<AuditLog> auditLogs = entityManager
				.createQuery("Select a from AuditLog a order by a.actedOn desc", AuditLog.class).getResultList();

		assertEquals(auditLogs.size(), 0);
	}

	@Test
	void shouldSaveAuditLogWhenRoleIsSoftDeleted() {
		Role role = new Role();
		role.setName("ABC");
		role.setPermissons("XYZ");
		role = this.roleRepository.save(role);
		List<Role> roles = this.roleRepository.findAll();
		assertEquals(roles.size(), 1);
		System.out.println(roles.size() == 1);
		try {
			this.roleRepository.softDeleteById(role.getId());
		}catch(Exception e) {
			assertEquals(e.getMessage(), "Forbidden :: User is not Authenticated");
		}

		roles = this.roleRepository.findAll();
		assertEquals(roles.size(), 1);

		List<AuditLog> auditLogs = entityManager
				.createQuery("Select a from AuditLog a order by a.actedOn desc", AuditLog.class).getResultList();

		assertEquals(auditLogs.size(), 0);
	}

}
