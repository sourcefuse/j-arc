package com.sourcefuse.jarc.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepositoryImpl;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
class CoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
