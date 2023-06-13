package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.FcmProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class FcmProviderTests {

  @Mock
  private FcmConnectionConfig fcmConnectionConfig;

  @Mock
  private FirebaseApp firebaseApp;

  @Mock
  private FirebaseMessaging firebaseMessaging;

  @InjectMocks
  private FcmProvider fcmProvider;

  Notification message;

  @BeforeEach
  void setup() throws FirebaseMessagingException {
    MockitoAnnotations.openMocks(this);

    Mockito.when(fcmConnectionConfig.getFirebaseApp()).thenReturn(firebaseApp);
    Mockito
      .when(fcmConnectionConfig.getFirebaseMessaging())
      .thenReturn(firebaseMessaging);
    Mockito
      .when(
        firebaseMessaging.sendMulticast(
          Mockito.any(MulticastMessage.class),
          Mockito.any(Boolean.class)
        )
      )
      .thenReturn(Mockito.any(BatchResponse.class));
    Mockito
      .when(firebaseMessaging.send(Mockito.any(Message.class), false))
      .thenReturn("messageId");

    message = MockNotifications.getFcmNotificationObj();
  }

  /**
   * successfully sends notification to all receivers on token
   *
   * @throws FirebaseMessagingException
   */
  @Test
  void testPublish_SendsNotificationToReceiversOnTokens()
    throws FirebaseMessagingException {
    fcmProvider.publish(message);

    Mockito
      .verify(firebaseMessaging, Mockito.times(1))
      .sendMulticast(Mockito.any(MulticastMessage.class), Mockito.anyBoolean());
  }

  /**
   * successfully sends notification to all receivers on topic
   *
   * @throws FirebaseMessagingException
   */
  @Test
  void testPublish_SendsNotificationToReceiversOnTopic()
    throws FirebaseMessagingException {
    message
      .getReceiver()
      .getTo()
      .stream()
      .forEach(item -> item.setType(FcmSubscriberType.FCM_CONDITION));
    fcmProvider.publish(message);

    Mockito
      .verify(firebaseMessaging, Mockito.times(2))
      .send(Mockito.any(Message.class), Mockito.anyBoolean());
  }

  /**
   * successfully sends notification to all receivers on condition
   *
   * @throws FirebaseMessagingException
   */
  @Test
  void testPublish_SendsNotificationToReceiversOnCondition()
    throws FirebaseMessagingException {
    message
      .getReceiver()
      .getTo()
      .stream()
      .forEach(item -> item.setType(FcmSubscriberType.FCM_CONDITION));
    fcmProvider.publish(message);

    Mockito
      .verify(firebaseMessaging, Mockito.times(2))
      .send(Mockito.any(Message.class), Mockito.anyBoolean());
  }

  /**
   * Fail to execute due to receivers are empty
   */
  @Test
  void testPublish_FailDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> fcmProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVER_OR_TOPIC_OR_CONDITION_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to execute due to receivers exceeded max receiver count i.e 500
   */
  @Test
  void testPublish_FailDuetoReceiversExceedsMaxReceiverCount() {
    List<Subscriber> subscribers = new ArrayList<>();
    for (int i = 0; i < 501; i++) {
      Subscriber subscriber = new Subscriber();
      subscriber.setId("userId" + i);
      subscribers.add(subscriber);
    }
    message.getReceiver().setTo(subscribers);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> fcmProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_EXCEEDS_500.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to execute due to subject of notification is empty
   */
  @Test
  void testPublish_FailDuetoEmptySubject() {
    message.setSubject(null);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> fcmProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.MESSAGE_TITLE_NOT_FOUND.toString(),
      exception.getReason()
    );
  }
}
