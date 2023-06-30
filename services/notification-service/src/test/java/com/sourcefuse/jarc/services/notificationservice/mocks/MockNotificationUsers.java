package com.sourcefuse.jarc.services.notificationservice.mocks;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import java.util.UUID;

public final class MockNotificationUsers {

  private MockNotificationUsers() {}

  public static final UUID NOTIFICATION_USER_ID = UUID.fromString(
    "45e6bb6b-3150-4cf3-9ca2-3bb299f5e29f"
  );

  public static NotificationUser getNotificationUser() {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setId(MockNotifications.NOTIFICATION_ID);

    NotificationUser notificationUser = new NotificationUser();
    notificationUser.setId(
      UUID.fromString("3237b45a-d843-4a64-a364-a2929e9a375f")
    );
    notificationUser.setNotification(notification);
    notificationUser.setUserId(
      notification.getReceiver().getTo().get(0).getId()
    );

    return notificationUser;
  }

  public static NotificationUser getNotificationUserRequestBody() {
    NotificationUser notificationUser = new NotificationUser();
    notificationUser.setUserId("dummyUserId");

    return notificationUser;
  }
}
