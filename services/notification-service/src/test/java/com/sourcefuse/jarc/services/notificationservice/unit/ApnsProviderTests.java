package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.notnoop.apns.ApnsService;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.apns.ApnsProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.push.apns.types.ApnsConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

class ApnsProviderTests {

  @Mock
  private ApnsConnectionConfig apnsConnectionConfig;

  @Mock
  private ApnsService apnsService;

  @InjectMocks
  private ApnsProvider apnsProvider;

  Notification message;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    Mockito.when(apnsConnectionConfig.getApnsService()).thenReturn(apnsService);
    Mockito.when(apnsConnectionConfig.getBadge()).thenReturn(1);
    Mockito.when(apnsConnectionConfig.getTopic()).thenReturn("Dummy-Topic");

    message = MockNotifications.getApnsNotificationObj();
  }

  /**
   * successfully sends push notification to all receiver
   */
  @Test
  void testPublish_SendsNotificationToReceivers() {
    apnsProvider.publish(message);

    Mockito
      .verify(apnsService, Mockito.times(1))
      .push(
        Mockito.anyList(),
        Mockito.any(String.class),
        Mockito.any(Date.class)
      );
  }

  /**
   * Fail to execute due to receivers are empty
   */
  @Test
  void testPublish_FailDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> apnsProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_NOT_FOUND.toString(),
      exception.getStatusText()
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
      subscriber.setId(UUID.randomUUID().toString());
      subscribers.add(subscriber);
    }
    message.getReceiver().setTo(subscribers);
    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> apnsProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_EXCEEDS_500.toString(),
      exception.getStatusText()
    );
  }

  /**
   * Fail to execute due to message from is empty
   */
  @Test
  void testPublish_FailDuetoEmptyMessageFromInOptions() {
    // set to from mail to empty
    message.getOptions().put("messageFrom", null);

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> apnsProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.MESSAGE_FROM_NOT_FOUND.toString(),
      exception.getStatusText()
    );
  }

  /**
   * Fail to execute due to subject of notification is empty
   */
  @Test
  void testPublish_FailDuetoEmptySubject() {
    message.setSubject(null);

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> apnsProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.MESSAGE_TITLE_NOT_FOUND.toString(),
      exception.getStatusText()
    );
  }
}
