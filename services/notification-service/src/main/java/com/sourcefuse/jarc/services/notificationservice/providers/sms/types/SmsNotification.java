package com.sourcefuse.jarc.services.notificationservice.providers.sms.types;

import com.sourcefuse.jarc.services.notificationservice.types.Message;

public interface SmsNotification {
  void publish(Message message);
}
