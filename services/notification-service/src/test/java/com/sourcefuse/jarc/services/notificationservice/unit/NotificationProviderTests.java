package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.NotificationProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.email.ses.SesProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.FcmProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.SnsProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

class NotificationProviderTests {

  SnsProvider snsProvider = Mockito.mock(SnsProvider.class);

  SesProvider sesProvider = Mockito.mock(SesProvider.class);

  FcmProvider fcmProvider = Mockito.mock(FcmProvider.class);

  @InjectMocks
  NotificationProvider notificationProvider;

  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testPublish_ShouldCallSMSPublish() {
    setUp();
    Notification message = MockNotifications.getSnsNotificationObj();

    notificationProvider.publish(message);

    Mockito.verify(snsProvider, Mockito.times(1)).publish(message);
  }

  @Test
  void testPublish_ShouldCallPUSHPublish() {
    setUp();
    Notification message = MockNotifications.getFcmNotificationObj();

    notificationProvider.publish(message);

    Mockito.verify(fcmProvider, Mockito.times(1)).publish(message);
  }

  @Test
  void testPublish_ShouldCallEMAILPublish() {
    setUp();
    Notification message = MockNotifications.getEmailNotificationObj();

    notificationProvider.publish(message);

    Mockito.verify(sesProvider, Mockito.times(1)).publish(message);
  }

  @Test
  void testPublish_ShouldFailDuetoSMSProviderIsNull() {
    snsProvider = null;
    MockitoAnnotations.openMocks(this);

    Notification message = MockNotifications.getSnsNotificationObj();

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> notificationProvider.publish(message)
    );

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals(
      NotificationError.PROVIDER_NOT_FOUND.toString(),
      exception.getStatusText()
    );
  }

  @Test
  void testPublish_ShouldFailDuetoPushProviderIsNull() {
    fcmProvider = null;
    MockitoAnnotations.openMocks(this);

    Notification message = MockNotifications.getFcmNotificationObj();

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> notificationProvider.publish(message)
    );

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals(
      NotificationError.PROVIDER_NOT_FOUND.toString(),
      exception.getStatusText()
    );
  }

  @Test
  void testPublish_ShouldFailDuetoEmailProviderIsNull() {
    sesProvider = null;
    MockitoAnnotations.openMocks(this);

    Notification message = MockNotifications.getEmailNotificationObj();

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> notificationProvider.publish(message)
    );

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals(
      NotificationError.PROVIDER_NOT_FOUND.toString(),
      exception.getStatusText()
    );
  }
}
