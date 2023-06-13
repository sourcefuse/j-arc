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

  public static final UUID NOTIFICATION_ID = UUID.fromString(
    "be847974-bc6b-4781-aa21-ecf876392cc0"
  );

  public static final UUID SUBSCRIBER_ONE_ID = UUID.fromString(
    "742f0244-7889-4d2c-a007-2906dc6ee7f7"
  );

  public static final UUID SUBSCRIBER_TWO_ID = UUID.fromString(
    "934d17d0-4c66-48b8-bbfc-1a16a9ec10c7"
  );

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
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

    Notification notification = buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );

    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("messageFrom", "DummyMessageFrom");

    notification.setOptions(options);
    return notification;
  }

  public static Notification getFcmNotificationObj() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    subscriberOne.setType(FcmSubscriberType.REGISTRATION_TOKEN);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());
    subscriberTwo.setType(FcmSubscriberType.REGISTRATION_TOKEN);

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );
  }

  public static Notification getPubnubNotificationObj() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    subscriberOne.setType(PubNubSubscriberType.CHANNEL);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());
    subscriberTwo.setType(PubNubSubscriberType.CHANNEL);

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );
  }

  public static Notification getSocketIoNotificationObj() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.PUSH
    );
  }

  public static Config getNotificationAccess() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());

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
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    subscriberOne.setType(TwilioSubscriberType.WHATSAPP_USER);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());
    subscriberTwo.setType(TwilioSubscriberType.WHATSAPP_USER);

    Notification notification = buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.SMS
    );

    HashMap<String, Object> options = new HashMap<String, Object>();

    notification.setOptions(options);
    return notification;
  }

  public static Notification getSnsNotificationObj() {
    Subscriber subscriberOne = new Subscriber();
    subscriberOne.setId(SUBSCRIBER_ONE_ID.toString());
    subscriberOne.setType(SnsSubscriberType.PHONE_NUMBER);
    Subscriber subscriberTwo = new Subscriber();
    subscriberTwo.setId(SUBSCRIBER_TWO_ID.toString());
    subscriberTwo.setType(SnsSubscriberType.PHONE_NUMBER);

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

    return buildNotification(
      Arrays.asList(subscriberOne, subscriberTwo),
      MessageType.SMS
    );
  }

  public static Notification dummyNotification() {
    Notification notification = getSnsNotificationObj();
    notification.setId(UUID.fromString("626cf583-d83a-4a06-8948-ab9b7ddf769c"));

    return notification;
  }
}
