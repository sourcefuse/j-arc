package com.sourcefuse.jarc.core.awares;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;

@Service
public class ApplicationAwareBeanUtils implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	public static EntityManager getNewEntityManager() throws NullPointerException {
		EntityManager em = context.getBean(EntityManager.class);
		if (em == null) {
			throw new NullPointerException("Entity Manager is null can not proceed to save audit log");
		}
		return em.getEntityManagerFactory().createEntityManager();
	}
}
