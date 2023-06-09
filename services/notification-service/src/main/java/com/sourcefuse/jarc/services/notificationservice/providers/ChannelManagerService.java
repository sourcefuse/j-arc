package com.sourcefuse.jarc.services.notificationservice.providers;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.notificationservice.types.Config;

public interface ChannelManagerService {
  Boolean isChannelAccessAllowed(CurrentUser currentUser, Config config);
}
