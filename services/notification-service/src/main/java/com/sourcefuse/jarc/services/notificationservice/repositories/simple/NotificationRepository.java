package com.sourcefuse.jarc.services.notificationservice.repositories.simple;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
  extends JpaRepository<Notification, UUID> {}
