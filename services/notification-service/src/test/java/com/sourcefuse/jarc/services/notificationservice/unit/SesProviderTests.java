package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.email.ses.SesProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.MailConnectionConfig;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;

class SesProviderTests {

  @Mock
  private MailConnectionConfig sesConnectionConfig;

  @Mock
  private JavaMailSender javaMailSender;

  @InjectMocks
  private SesProvider sesProvider;

  Notification message;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    Mockito
      .when(sesConnectionConfig.getJavaMailSender())
      .thenReturn(javaMailSender);
    Mockito
      .when(sesConnectionConfig.getSenderMail())
      .thenReturn("abc@example.com");
    Mockito
      .when(sesConnectionConfig.shouldSendToMultipleReceivers())
      .thenReturn(true);

    message = MockNotifications.getEmailNotificationObj();
  }

  /**
   * successfully sends single mail to all receiver
   */
  @Test
  void testPublish_SendsMailToReceivers() {
    sesProvider.publish(message);

    Mockito
      .verify(javaMailSender, Mockito.times(1))
      .send(any(SimpleMailMessage.class));
  }

  /**
   * successfully send multiple mails to each receiver seperately
   */
  @Test
  void testPublish_SendsMultipleMailToReceivers() {
    //set to send mail to each user seperately
    Mockito
      .when(sesConnectionConfig.shouldSendToMultipleReceivers())
      .thenReturn(false);

    sesProvider.publish(message);

    Mockito
      .verify(javaMailSender, Mockito.times(2))
      .send(any(SimpleMailMessage.class));
  }

  /**
   * Fail to execute due to receivers are empty
   */
  @Test
  void testPublish_FailDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> sesProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to execute due to sender mail is empty
   */
  @Test
  void testPublish_FailDuetoEmptyFromMail() {
    // set to from mail to empty
    Mockito.when(sesConnectionConfig.getSenderMail()).thenReturn(null);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> sesProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.SENDER_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to execute due to subject of mail is empty
   */
  @Test
  void testPublish_FailDuetoEmptySubject() {
    message.setSubject(null);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> sesProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.MESSAGE_DATA_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to execute due to body of mail is empty
   */
  @Test
  void testPublish_FailDuetoEmptyBody() {
    message.setSubject(null);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> sesProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.MESSAGE_DATA_NOT_FOUND.toString(),
      exception.getReason()
    );
  }
}
