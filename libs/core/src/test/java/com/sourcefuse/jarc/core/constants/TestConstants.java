package com.sourcefuse.jarc.core.constants;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestConstants {

  public static UUID mockUserId = UUID.fromString(
    "d262e1bf-1be8-3bac-ce49-166324df1e33"
  );

  public static EntityManager clearTables(EntityManager entityManager) {
    EntityManager em = entityManager
      .getEntityManagerFactory()
      .createEntityManager();
    System.out.println("reps");
    em.getTransaction().begin();
    try {
      em.createNativeQuery("DELETE FROM main.users;").executeUpdate();
      em.createNativeQuery("DELETE FROM main.roles;").executeUpdate();
      em.createNativeQuery("DELETE FROM logs.audit_logs;").executeUpdate();
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
    CurrentUser currentUser = createMockCurrentUser();
    // Create an authentication object with the dummy user and set it in the
    // SecurityContext
    UsernamePasswordAuthenticationToken auth =
      new UsernamePasswordAuthenticationToken(currentUser, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static CurrentUser createMockCurrentUser() {
    // Create a dummy user object
    CurrentUser currentUser = new CurrentUser();
    currentUser.setId(mockUserId);
    currentUser.setFirstName("Dummy");
    currentUser.setLastName("User");
    currentUser.setEmail("dummy.user@example.com");
    currentUser.setUsername("dummy.user@example.com");

    return currentUser;
  }
}
