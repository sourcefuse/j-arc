package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.TwilioProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioConnectionConfig;
import com.twilio.rest.api.v2010.account.MessageCreator;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

class TwilioProviderTests {

  @Mock
  private TwilioConnectionConfig twilioConnectionConfig;

  @Mock
  private MessageCreator messageCreator;

  @InjectMocks
  private TwilioProvider twilioProvider;

  Notification message;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    Mockito.when(twilioConnectionConfig.getSmsFrom()).thenReturn("+1234567890");
    Mockito
      .when(twilioConnectionConfig.getSmsStatusCallback())
      .thenReturn(null);
    Mockito
      .when(twilioConnectionConfig.getWhatsappFrom())
      .thenReturn("whatsapp:+1234567890");
    Mockito
      .when(twilioConnectionConfig.getWhatsappStatusCallback())
      .thenReturn(null);

    message = MockNotifications.getWhatsappTwilioNotificationObj();
  }

  /**
   * Failes to send SMS due to empty receivers
   */
  @Test
  void testPublish_FailsDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    HttpServerErrorException exception = assertThrows(
      HttpServerErrorException.class,
      () -> twilioProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_NOT_FOUND.toString(),
      exception.getStatusText()
    );
  }
}
