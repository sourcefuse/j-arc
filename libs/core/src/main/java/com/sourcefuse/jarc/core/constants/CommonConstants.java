package com.sourcefuse.jarc.core.constants;

public final class CommonConstants {

  public static final String NO_ROLE_PRESENT =
    "No role is present against given value";
  public static final String NO_GRP_PRESENT =
    "No group is present against given value";
  public static final String NO_USR_GRP_PRESENT =
    "No User group is present against given value";
  public static final String FIND = "FIND";
  public static final Integer SUPER_ADMIN_ROLE_TYPE = 10;
  public static final int TWO = 2;
  public static final String TENANT_ID = "tenantId";
  public static final String AUTH_PROVIDER = "authProvider";
  public static final String MESSAGE_PROPERTIES =
    "classpath:message.properties";
  public static final String USER_TENANT_SERVICE_PACKAGE =
    "com.sourcefuse.jarc.services.usertenantservice";
  public static final String CORE_PACKAGE = "com.sourcefuse.jarc.core";
  public static final String USER_TENANT_SERVICE_DTO_PACKAGE =
    "com.sourcefuse.jarc.services.usertenantservice.dto";
  public static final String CORE_MODEL_PACKAGE =
    "com.sourcefuse.jarc.core.models";
  public static final String CORE_SOFT_DELETE_PACKAGE =
    "com.sourcefuse.jarc.services.usertenantservice.repository";
  public static final String NO_RECORD =
    "No records is present against the given value";
  public static final String VIEW = "view";
  public static final String UPDATE = "update";
  public static final String DELETE = "delete";
  public static final String KEYCLOAK = "KEYCLOAK";
  private CommonConstants() {
    throw new IllegalStateException("Utility class");
  }
}
