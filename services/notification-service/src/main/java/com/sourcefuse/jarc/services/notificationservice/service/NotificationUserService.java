package com.sourcefuse.jarc.services.notificationservice.service;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class NotificationUserService {

  public List<NotificationUser> getNotifUsers(Notification notification) {
    return notification
      .getReceiver()
      .getTo()
      .stream()
      .map((Subscriber to) -> {
        NotificationUser notifUser = new NotificationUser();
        notifUser.setNotification(notification);
        notifUser.setUserId(to.getId());
        return notifUser;
      })
      .collect(Collectors.toList());
  }
}
