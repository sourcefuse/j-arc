package com.sourcefuse.jarc.services.notificationservice.repositories.softdelete;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import java.util.UUID;

public interface NotificationUserRepository
  extends SoftDeletesRepository<NotificationUser, UUID> {}
