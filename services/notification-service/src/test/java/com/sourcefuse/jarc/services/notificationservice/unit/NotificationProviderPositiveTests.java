package com.sourcefuse.jarc.services.notificationservice.unit;

import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.NotificationProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.email.ses.SesProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.FcmProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.SnsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class NotificationProviderPositiveTests {

  @Mock
  SnsProvider snsProvider;

  @Mock
  SesProvider sesProvider;

  @Mock
  FcmProvider fcmProvider;

  @InjectMocks
  NotificationProvider notificationProvider;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testPublish_ShouldCallSMSPublish() {
    Notification message = MockNotifications.getSnsNotificationObj();

    notificationProvider.publish(message);

    Mockito.verify(snsProvider, Mockito.times(1)).publish(message);
  }

  @Test
  void testPublish_ShouldCallPUSHPublish() {
    Notification message = MockNotifications.getFcmNotificationObj();

    notificationProvider.publish(message);

    Mockito.verify(fcmProvider, Mockito.times(1)).publish(message);
  }

  @Test
  void testPublish_ShouldCallEMAILPublish() {
    Notification message = MockNotifications.getEmailNotificationObj();

    notificationProvider.publish(message);

    Mockito.verify(sesProvider, Mockito.times(1)).publish(message);
  }
}
