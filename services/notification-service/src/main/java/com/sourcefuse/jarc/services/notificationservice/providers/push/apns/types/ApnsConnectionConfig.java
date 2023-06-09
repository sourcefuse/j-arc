package com.sourcefuse.jarc.services.notificationservice.providers.push.apns.types;

import com.notnoop.apns.ApnsService;

public interface ApnsConnectionConfig {
  public ApnsService getApnsService();

  public int getBadge();

  public String getTopic();
}
