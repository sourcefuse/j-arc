package com.sourcefuse.jarc.services.usertenantservice;

import com.sourcefuse.jarc.services.usertenantservice.dto.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTenantServiceApplicationTests {

  @Test
  void contextLoads() {
    Tenant tenant = new Tenant();
    tenant.setAddress("Mumbai");
    System.out.println(tenant);
  }
}
