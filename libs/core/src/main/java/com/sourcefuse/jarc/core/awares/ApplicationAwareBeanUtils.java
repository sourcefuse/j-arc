package com.sourcefuse.jarc.core.awares;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationAwareBeanUtils {

  @Autowired
  private ApplicationContext applicationContext;

  public <T> T getBean(Class<T> beanClass) {
    return applicationContext.getBean(beanClass);
  }

  public EntityManager getNewEntityManager() {
    EntityManager em = applicationContext.getBean(EntityManager.class);
    return em.getEntityManagerFactory().createEntityManager();
  }
}
