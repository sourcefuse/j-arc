package com.sourcefuse.jarc.core;

import com.sourcefuse.jarc.core.softdelete.SoftDeletesRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
class CoreApplicationTests {

  @Test
  void contextLoads() {}
}
