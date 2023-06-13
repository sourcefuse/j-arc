package com.sourcefuse.jarc.services.notificationservice.providers.email.ses;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.EmailNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.SesConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@ConditionalOnProperty(
  value = "notification.provider.email",
  havingValue = "sesProvider"
)
@RequiredArgsConstructor
@Slf4j
public class SesProvider implements EmailNotification {

  private static final String FROM_KEY = "from";

  private final SesConnectionConfig sesConnectionConfig;

  @Override
  public void publish(Message message) {
    String fromEmail = Optional
      .ofNullable(message.getOptions())
      .map(options -> (String) options.get(FROM_KEY))
      .orElse(this.sesConnectionConfig.getSenderMail());

    this.initialValidations(fromEmail, message);

    try {
      if (this.sesConnectionConfig.shouldSendToMultipleReceivers()) {
        this.sendToReceiversCombine(fromEmail, message);
      } else {
        this.sendToEachReceiverSeperately(fromEmail, message);
      }
    } catch (MailException e) {
      log.error(null, e);
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        NotificationError.SOMETHING_WNET_WRONG.toString()
      );
    }
  }

  void initialValidations(String fromEmail, Message message) {
    if (fromEmail == null || fromEmail.isBlank()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.SENDER_NOT_FOUND.toString()
      );
    }

    if (message.getReceiver().getTo().isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_NOT_FOUND.toString()
      );
    }
    if (
      message.getSubject() == null ||
      message.getSubject().isBlank() ||
      message.getBody() == null ||
      message.getBody().isBlank()
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.MESSAGE_DATA_NOT_FOUND.toString()
      );
    }
  }

  void sendToReceiversCombine(String fromEmail, Message message) {
    String[] receivers = message
      .getReceiver()
      .getTo()
      .stream()
      .map(Subscriber::getId)
      .toArray(String[]::new);
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(fromEmail);
    simpleMailMessage.setTo(receivers);
    simpleMailMessage.setSubject(message.getSubject());
    simpleMailMessage.setText(message.getBody());

    sesConnectionConfig.getJavaMailSender().send(simpleMailMessage);
  }

  void sendToEachReceiverSeperately(String fromEmail, Message message) {
    for (Subscriber to : message.getReceiver().getTo()) {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom(fromEmail);
      simpleMailMessage.setTo(to.getId());
      simpleMailMessage.setSubject(message.getSubject());
      simpleMailMessage.setText(message.getBody());

      sesConnectionConfig.getJavaMailSender().send(simpleMailMessage);
    }
  }
}
