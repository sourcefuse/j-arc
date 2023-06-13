package com.sourcefuse.jarc.services.notificationservice.providers.push.apns.types;

import com.notnoop.apns.ApnsService;

public interface ApnsConnectionConfig {
  ApnsService getApnsService();

  int getBadge();

  String getTopic();
}
