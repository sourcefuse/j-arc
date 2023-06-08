package com.sourcefuse.jarc.services.notificationservice.mocks;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.sourcefuse.jarc.services.notificationservice.enums.MessageType;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.types.Receiver;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;

public final class MockNotifications {

	private static Notification buildNotification(List<Subscriber> subscribers, MessageType type) {

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

		return buildNotification(Arrays.asList(subscriberOne, subscriberTwo), MessageType.EMAIL);
	}

	public static Notification getApnsNotificationObj() {
		// Create a test message
		Subscriber subscriberOne = new Subscriber();
		subscriberOne.setId(UUID.randomUUID().toString());
		Subscriber subscriberTwo = new Subscriber();
		subscriberTwo.setId(UUID.randomUUID().toString());

		Receiver receiver = new Receiver();
		receiver.setTo(Arrays.asList(subscriberOne, subscriberTwo));

		return buildNotification(Arrays.asList(subscriberOne, subscriberTwo), MessageType.PUSH);
	}

	public static Notification getFcmNotificationObj() {
		// Create a test message
		Subscriber subscriberOne = new Subscriber();
		subscriberOne.setId(UUID.randomUUID().toString());
		subscriberOne.setType(FcmSubscriberType.RegistrationToken);
		Subscriber subscriberTwo = new Subscriber();
		subscriberTwo.setId(UUID.randomUUID().toString());
		subscriberTwo.setType(FcmSubscriberType.RegistrationToken);

		return buildNotification(Arrays.asList(subscriberOne, subscriberTwo), MessageType.PUSH);
	}

	public static Notification getPubnubNotificationObj() {
		// Create a test message
		Subscriber subscriberOne = new Subscriber();
		subscriberOne.setId(UUID.randomUUID().toString());
		subscriberOne.setType(PubNubSubscriberType.Channel);
		Subscriber subscriberTwo = new Subscriber();
		subscriberTwo.setId(UUID.randomUUID().toString());
		subscriberTwo.setType(PubNubSubscriberType.Channel);

		return buildNotification(Arrays.asList(subscriberOne, subscriberTwo), MessageType.PUSH);
	}
}
