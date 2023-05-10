package com.sourcefuse.jarc.core.awares;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationAwareBeanUtils {

  private ApplicationContext context;

  public <T> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);
  }

  public EntityManager getNewEntityManager() {
    EntityManager em = context.getBean(EntityManager.class);
    return em.getEntityManagerFactory().createEntityManager();
  }
}
