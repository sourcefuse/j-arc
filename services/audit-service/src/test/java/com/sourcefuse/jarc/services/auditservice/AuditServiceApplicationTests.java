package com.sourcefuse.jarc.services.auditservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.services.auditservice.audit.softdelete.SoftDeletesRepositoryImpl;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
class AuditServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
