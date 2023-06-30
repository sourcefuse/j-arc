package com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types;

import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Config;

public interface PubNubNotification extends PushNotification {
  Object grantAccess(Config config);

  Object revokeAccess(Config config);
}
