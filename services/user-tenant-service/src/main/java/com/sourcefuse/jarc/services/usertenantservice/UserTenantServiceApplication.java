package com.sourcefuse.jarc.services.usertenantservice;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({ CommonConstants.MESSAGE_PROPERTIES })
@ComponentScan(
  basePackages = {
    CommonConstants.USER_TENANT_SERVICE_PACKAGE, CommonConstants.CORE_PACKAGE,
  }
)
@EntityScan(
  basePackages = {
    CommonConstants.USER_TENANT_SERVICE_DTO_PACKAGE,
    CommonConstants.CORE_MODEL_PACKAGE,
  }
)
public class UserTenantServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserTenantServiceApplication.class, args);
  }
}
