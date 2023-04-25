//package com.sourcefuse.jarc.services.auditservice.audit.repository;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.DirtiesContext.ClassMode;
//
//import com.sourcefuse.jarc.services.auditservice.audit.repositories.UserRepository;
//import com.sourcefuse.jarc.services.auditservice.constants.Constants.AuditActions;
//import com.sourcefuse.jarc.services.auditservice.audit.models.AuditLog;
//import com.sourcefuse.jarc.services.auditservice.models.User;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//@SpringBootTest
//public class AuditRepositoryMixinTests {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@BeforeEach
//	void clearUserAndAuditLog() {
//		System.out.println("reps");
////		entityManager.getTransaction().begin();
//		try {
////		entityManager.createNativeQuery("TRUNCATE TABLE main.user cascade;").executeUpdate();
////		entityManager.createNativeQuery("TRUNCATE TABLE main.audit_log cascade;").executeUpdate();
////		entityManager.getTransaction().commit();
//		}catch(Exception e) {
//			e.printStackTrace();
////			entityManager.getTransaction().rollback();
//		}
//	}
//
//	@Test
//	public void saveSingleTest() {
//		try {
//			User user = new User();
//			user.setFirstName("ABC");
//			user.setLastName("XYZ");
//			user = this.userRepository.save(user);
//			List<User> users = this.userRepository.findAll();
//			assertEquals(users.size(), 1);
//			assertEquals(user, users.get(0));
//
//			List<AuditLog> auditLogs = entityManager.createQuery("Select a from AuditLog a", AuditLog.class)
//					.getResultList();
//
//			assertEquals(auditLogs.size(), 1);
//			assertEquals(auditLogs.get(0).getAction(), AuditActions.SAVE);
//			assertEquals(auditLogs.get(0).getEntityId(), user.getId());
//			assertEquals(auditLogs.get(0).getActedAt(), user.getTableName());
//			assertEquals(auditLogs.get(0).getActionKey(), "User_Logs");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	@Test
//	public void test2() {
//		try {
//			User user = new User();
//			user.setFirstName("ABC");
//			user.setLastName("XYZ");
//			user = this.userRepository.save(user);
//			List<User> users = this.userRepository.findAll();
//			assertEquals(users.size(), 1);
//			assertEquals(user, users.get(0));
//
//			List<AuditLog> auditLogs = entityManager.createQuery("Select a from AuditLog a", AuditLog.class)
//					.getResultList();
//
//			assertEquals(auditLogs.size(), 1);
//			assertEquals(auditLogs.get(0).getAction(), AuditActions.SAVE);
//			assertEquals(auditLogs.get(0).getEntityId(), user.getId());
//			assertEquals(auditLogs.get(0).getActedAt(), user.getTableName());
//			assertEquals(auditLogs.get(0).getActionKey(), "User_Logs");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//}
