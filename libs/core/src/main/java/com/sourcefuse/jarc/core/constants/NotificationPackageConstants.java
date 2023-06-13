package com.sourcefuse.jarc.core.constants;

public final class NotificationPackageConstants {

  public static final String SERVICE_PACKAGE =
    "com.sourcefuse.jarc.services.notificationservice";

  public static final String SOFTDELETE_REPO_PACKAGE =
    SERVICE_PACKAGE + ".repositories.softdelete";

  public static final String JPA_REPO_PACKAGE =
    SERVICE_PACKAGE + ".repositories.simple";

  private NotificationPackageConstants() {}
}
