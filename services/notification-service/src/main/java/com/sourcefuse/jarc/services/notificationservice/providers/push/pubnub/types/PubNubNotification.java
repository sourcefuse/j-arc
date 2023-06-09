package com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types;

import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Config;

public interface PubNubNotification extends PushNotification {
  public Object grantAccess(Config config);

  public Object revokeAccess(Config config);
}
