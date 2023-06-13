package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.SnsProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SnsConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SnsSubscriberType;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

class SnsProviderTests {

  @Mock
  private SnsConnectionConfig snsConnectionConfig;

  @Mock
  private SnsClient snsClient;

  @InjectMocks
  private SnsProvider snsProvider;

  Notification message;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    Mockito.when(snsConnectionConfig.getSnsClient()).thenReturn(snsClient);

    message = MockNotifications.getSnsNotificationObj();
  }

  /**
   * successfully sends sms on phone number
   */
  @Test
  void testPublish_SendsSMSToReceiversOnPhoneNumber() {
    message
      .getReceiver()
      .getTo()
      .stream()
      .forEach(item -> item.setType(SnsSubscriberType.PHONE_NUMBER));
    snsProvider.publish(message);

    Mockito
      .verify(snsClient, Mockito.times(2))
      .publish(Mockito.any(PublishRequest.class));
  }

  /**
   * successfully sends sms on topic arn
   */
  @Test
  void testPublish_SendsSMSToReceiversOnTopicArn() {
    message
      .getReceiver()
      .getTo()
      .stream()
      .forEach(item -> item.setType(SnsSubscriberType.TOPIC));
    snsProvider.publish(message);

    Mockito
      .verify(snsClient, Mockito.times(2))
      .publish(Mockito.any(PublishRequest.class));
  }

  /**
   * Fail to execute due to receivers are empty
   */
  @Test
  void testPublish_FailDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> snsProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_NOT_FOUND.toString(),
      exception.getReason()
    );
  }
}
