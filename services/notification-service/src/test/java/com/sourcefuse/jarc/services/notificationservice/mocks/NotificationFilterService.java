package com.sourcefuse.jarc.services.notificationservice.mocks;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.types.INotificationFilterFunc;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotificationFilterService implements INotificationFilterFunc {

  List<String> blacklistedUsers = Arrays.asList(
    MockNotifications.BLOCKED_USER_ONE_ID.toString(),
    MockNotifications.BLOCKED_USER_TWO_ID.toString()
  );

  @Override
  public Notification filter(Notification notification) {
    List<Subscriber> receivers = notification
      .getReceiver()
      .getTo()
      .stream()
      .filter((Subscriber subscriber) ->
        !blacklistedUsers.contains(subscriber.getId())
      )
      .toList();
    notification.getReceiver().setTo(receivers);
    return notification;
  }
}
