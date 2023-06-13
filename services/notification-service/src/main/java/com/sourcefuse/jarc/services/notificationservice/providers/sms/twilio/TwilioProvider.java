package com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class TwilioProvider implements SmsNotification {

  private final TwilioConnectionConfig twilioConnection;

  private static final String MEDIA_URL_KEY = "mediaUrl";

  @Override
  public void publish(Message message) {
    if (message.getReceiver().getTo().size() == 0) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Message receiver not found in request"
      );
    }

    for (Subscriber receiver : message.getReceiver().getTo()) {
      sendMessage(message, receiver);
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
        .equals(TwilioSubscriberType.TEXT_SMS_USER.toString())
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
        (List<URI>) message.getOptions().get(MEDIA_URL_KEY)
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
