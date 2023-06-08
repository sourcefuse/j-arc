package com.sourcefuse.jarc.services.notificationservice.providers.push.types;

import com.sourcefuse.jarc.services.notificationservice.types.Message;

public interface PushNotification {
  public void publish(Message message);
}
