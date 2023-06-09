package com.sourcefuse.jarc.services.notificationservice.mocks;

import com.sourcefuse.jarc.services.notificationservice.enums.MessageType;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SnsSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.types.Config;
import com.sourcefuse.jarc.services.notificationservice.types.Receiver;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class MockNotifications {

  private static Notification buildNotification(
    List<Subscriber> subscribers,
    MessageType type
  ) {
    Receiver receiver = new Receiver();
    receiver.setTo(subscribers);

    Notification message = new Notification();
    message.setBody("Dummy Body");
    message.setSubject("Dummy Subject");
    message.setSentDate(LocalDateTime.now());
    message.setReceiver(receiver);
    message.setType(type);
    return message;
  }

  public static Notification getEmailNotificationObj() {
    // Create a test message
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId("subscriber.one@example.com");
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId("subscriber.two@example.com");

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.EMAIL
    );
  }

  public static Notification getApnsNotificationObj() {
    // Create a test message
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(UUID.randomUUID().toString());
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(UUID.randomUUID().toString());

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );
  }

  public static Notification getFcmNotificationObj() {
    // Create a test message
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(UUID.randomUUID().toString());
    subscriberOne.setType(FcmSubscriberType.RegistrationToken);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(UUID.randomUUID().toString());
    subscriberTwo.setType(FcmSubscriberType.RegistrationToken);

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );
  }

  public static Notification getPubnubNotificationObj() {
    // Create a test message
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(UUID.randomUUID().toString());
    subscriberOne.setType(PubNubSubscriberType.Channel);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(UUID.randomUUID().toString());
    subscriberTwo.setType(PubNubSubscriberType.Channel);

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );
  }

  public static Config getNotificationAccess() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(UUID.randomUUID().toString());
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(UUID.randomUUID().toString());

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("ttl", 100);
    options.put("token", "DummyToken");

    Config notificationAccess = new Config();
    notificationAccess.setOptions(options);
    notificationAccess.setReceiver(receiver);
    notificationAccess.setType(MessageType.EMAIL);
    return notificationAccess;
  }

  public static Notification getWhatsappTwilioNotificationObj() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(UUID.randomUUID().toString());
    subscriberOne.setType(TwilioSubscriberType.WhatsappUser);
    Subscriber subscriberTwo = new Subscriber();
    subscriberOne.setType(TwilioSubscriberType.WhatsappUser);
    subscriberTwo.setId(UUID.randomUUID().toString());

    Notification notification = buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.SMS
    );

    HashMap<String, Object> options = new HashMap<String, Object>();

    notification.setOptions(options);
    return notification;
  }

  public static Notification getSnsNotificationObj() {
    // Create a test message
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(UUID.randomUUID().toString());
    subscriberOne.setType(SnsSubscriberType.PhoneNumber);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(UUID.randomUUID().toString());
    subscriberOne.setType(SnsSubscriberType.PhoneNumber);

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.SMS
    );
  }
}
