package com.sourcefuse.jarc.core.constants;

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.core.test.models.User;

import jakarta.persistence.EntityManager;

public class TestConstants {

	public static UUID mockUserId = UUID.fromString("d262e1bf-1be8-3bac-ce49-166324df1e33");

	public static EntityManager clearTables(EntityManager entityManager) {
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		System.out.println("reps");
		em.getTransaction().begin();
		try {
			em.createNativeQuery("TRUNCATE TABLE main.roles cascade;").executeUpdate();
			em.createNativeQuery("TRUNCATE TABLE logs.audit_logs cascade;").executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return em;
	}

	public static void setCurrentLoggedInUser() {
		// Create a dummy user object
		User user = new User();
		user.setId(mockUserId);
		user.setFirstName("Oasis");
		user.setLastName("Admin");
		user.setEmail("oasis_admin@sourcefuse.com");
		user.setUsername("oasis_admin@sourcefuse.com");
		CurrentUser<User> currentUser = new CurrentUser<User>();
		currentUser.setUser(user);
		// Create an authentication object with the dummy user and set it in the
		// SecurityContext
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(currentUser, null, null);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
