package com.sourcefuse.jarc.services.notificationservice.mocks;

import com.sourcefuse.jarc.services.notificationservice.enums.MessageType;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationAccess;
import com.sourcefuse.jarc.services.notificationservice.types.Receiver;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.Arrays;

public final class MockNotificationAccess {

  public static NotificationAccess getEmailNotificationObj() {
    // Create a test message
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId("subscriber.one@example.com");
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId("subscriber.two@example.com");

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

    NotificationAccess notificationAccess = new NotificationAccess();
    notificationAccess.setReceiver(receiver);
    notificationAccess.setType(MessageType.EMAIL);

    return notificationAccess;
  }
}
