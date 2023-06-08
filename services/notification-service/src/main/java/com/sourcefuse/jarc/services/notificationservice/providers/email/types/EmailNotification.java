package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import com.sourcefuse.jarc.services.notificationservice.types.Message;

public interface EmailNotification {
  public void publish(Message message);
}
