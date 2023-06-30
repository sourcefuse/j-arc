package com.sourcefuse.jarc.services.notificationservice.types;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;

public interface INotificationFilterFunc {
  Notification filter(Notification notif);
}
