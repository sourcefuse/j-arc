package com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@ConditionalOnProperty(
  value = "notification.provider.sms",
  havingValue = "TwilioProvider"
)
@RequiredArgsConstructor
@Slf4j
public class TwilioProvider implements SmsNotification {

  private static final String MEDIA_URL_KEY = "mediaUrl";

  private final TwilioConnectionConfig twilioConnection;

  @Override
  public void publish(Message message) {
    if (message.getReceiver().getTo().isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Message receiver not found in request"
      );
    }

    for (Subscriber receiver : message.getReceiver().getTo()) {
      try {
        sendMessage(message, receiver);
      } catch (ApiConnectionException | ApiException e) {
        log.error(null, e);
        throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          NotificationError.SOMETHING_WNET_WRONG.toString()
        );
      }
    }
  }

  @SuppressWarnings("unchecked")
  void sendMessage(Message message, Subscriber receiver) {
    String from = twilioConnection.getSmsFrom();
    String to = "+" + receiver.getId();
    if (
      receiver.getType() != null &&
      receiver
        .getType()
        .toString()
        .equals(TwilioSubscriberType.WHATSAPP_USER.toString())
    ) {
      from = twilioConnection.getWhatsappFrom();
      to = "whatsapp:+" + receiver.getId();
    }

    MessageCreator messageCreator = new MessageCreator(
      new PhoneNumber(to),
      new PhoneNumber(from),
      message.getBody()
    );

    if (message.getOptions().get(MEDIA_URL_KEY) != null) {
      messageCreator.setMediaUrl(
        ((List<String>) message.getOptions().get(MEDIA_URL_KEY)).stream()
          .map(URI::create)
          .toList()
      );
    }
    if (
      receiver.getType() != null &&
      receiver
        .getType()
        .toString()
        .equals(TwilioSubscriberType.TEXT_SMS_USER.toString()) &&
      twilioConnection.getSmsStatusCallback() != null
    ) {
      messageCreator.setStatusCallback(twilioConnection.getSmsStatusCallback());
    }
    if (
      receiver.getType() == null &&
      twilioConnection.getWhatsappStatusCallback() != null
    ) {
      messageCreator.setStatusCallback(
        twilioConnection.getWhatsappStatusCallback()
      );
    }
    messageCreator.create();
  }
}
