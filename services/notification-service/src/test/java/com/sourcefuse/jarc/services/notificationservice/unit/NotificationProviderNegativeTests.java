package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.NotificationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class NotificationProviderPossitiveTests {

  @InjectMocks
  NotificationProvider notificationProvider;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testPublish_ShouldFailDuetoSMSProviderIsNull() {
    Notification message = MockNotifications.getSnsNotificationObj();

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> notificationProvider.publish(message)
    );

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals(
      NotificationError.PROVIDER_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  @Test
  void testPublish_ShouldFailDuetoPushProviderIsNull() {
    Notification message = MockNotifications.getFcmNotificationObj();

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> notificationProvider.publish(message)
    );

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals(
      NotificationError.PROVIDER_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  @Test
  void testPublish_ShouldFailDuetoEmailProviderIsNull() {
    Notification message = MockNotifications.getEmailNotificationObj();

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> notificationProvider.publish(message)
    );

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals(
      NotificationError.PROVIDER_NOT_FOUND.toString(),
      exception.getReason()
    );
  }
}
