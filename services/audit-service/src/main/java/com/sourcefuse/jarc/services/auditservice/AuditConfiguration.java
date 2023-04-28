package com.sourcefuse.jarc.services.auditservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class AuditConfiguration {

//	@PersistenceContext
//	EntityManager entityManager;
//	
//	@Bean
//    @Qualifier("newEntityManager")
//	public EntityManager newEntityManager() {
//		EntityManager em =  entityManager.getEntityManagerFactory().createEntityManager();
//		System.out.println("-------------------------------------");
//		System.out.println(em);
//		System.out.println("-------------------------------------");
//		return em;
//	}
}
