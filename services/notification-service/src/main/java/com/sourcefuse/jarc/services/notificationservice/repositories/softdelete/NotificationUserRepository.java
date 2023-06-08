package com.sourcefuse.jarc.services.notificationservice.repositories.softdelete;

import java.util.UUID;

import com.sourcefuse.jarc.core.repositories.SoftDeletesRepository;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;

public interface NotificationUserRepository extends SoftDeletesRepository<NotificationUser, UUID> {

}
