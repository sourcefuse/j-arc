package com.sourcefuse.jarc.services.usertenantservice;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({ CommonConstants.MESSAGE_PROPERTIES })
@ComponentScan(
  basePackages = {
    CommonConstants.USER_TENANT_SERVICE_PACKAGE,
    CommonConstants.CORE_PACKAGE,
    "com.sourcefuse.jarc.authlib",
  }
)
@EntityScan(
  basePackages = {
    CommonConstants.USER_TENANT_SERVICE_DTO_PACKAGE,
    CommonConstants.CORE_MODEL_PACKAGE,
  }
)
@SecurityScheme(
  name = "bearerAuth",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
public class UserTenantServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserTenantServiceApplication.class, args);
  }
}
