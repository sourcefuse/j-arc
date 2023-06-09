package com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types;

import software.amazon.awssdk.services.sns.SnsClient;

public interface SnsConnectionConfig {
  public SnsClient getSnsClient();
}
