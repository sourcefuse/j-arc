package com.sourcefuse.jarc.services.notificationservice.repositories.redis;

import com.sourcefuse.jarc.services.notificationservice.models.NotificationAccess;
import java.util.UUID;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface NotificationAccessRepository
  extends KeyValueRepository<NotificationAccess, UUID> {}
