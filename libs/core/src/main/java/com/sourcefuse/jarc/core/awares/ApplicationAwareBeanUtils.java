package com.sourcefuse.jarc.core.awares;

import jakarta.persistence.EntityManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationAwareBeanUtils implements ApplicationContextAware {

  private ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
    throws BeansException {
    context = applicationContext;
  }

  public <T> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);
  }

  public EntityManager getNewEntityManager() {
    EntityManager em = context.getBean(EntityManager.class);
    return em.getEntityManagerFactory().createEntityManager();
  }
}
