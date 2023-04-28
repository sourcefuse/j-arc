//package com.sourcefuse.jarc.services.auditservice.audit.softdelete;
//
//import java.io.Serializable;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
//import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
//import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
//import org.springframework.data.repository.core.RepositoryInformation;
//import org.springframework.data.repository.core.RepositoryMetadata;
//import org.springframework.data.repository.core.support.RepositoryFactorySupport;
//
//import jakarta.persistence.EntityManager;
//
//public class CustomJpaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
//		extends JpaRepositoryFactoryBean<T, S, ID> {
//
//	public CustomJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
//		super(repositoryInterface);
//	}
//	
//	@Override
//	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
//		return new CustomJpaRepositoryFactory<T, ID>(entityManager);
//	}
//
//	private static class CustomJpaRepositoryFactory<T, ID extends Serializable> extends JpaRepositoryFactory {
//
//		@SuppressWarnings("unused")
//		private final EntityManager entityManager;
//
//		public CustomJpaRepositoryFactory(EntityManager entityManager) {
//			super(entityManager);
//			this.entityManager = entityManager;
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
//				EntityManager entityManager) {
//			return new SoftDeletesRepositoryImpl<T, ID>((Class<T>) information.getDomainType(), entityManager);
//		}
//
//		@Override
//		protected Class<?> getRepositoryBaseClass(RepositoryMetadata information) {
//			return SoftDeletesRepositoryImpl.class;
//		}
//	}
//}