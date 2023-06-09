package com.sourcefuse.jarc.services.notificationservice.providers.email.ses;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.EmailNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.SesConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@ConditionalOnProperty(
  value = "notification.provider.email",
  havingValue = "SESProvider"
)
public class SesProvider implements EmailNotification {

  @Autowired
  SesConnectionConfig mailConnectionConfig;

  @Override
  public void publish(Message message) {
    String fromEmail = message.getOptions() != null &&
      message.getOptions().get("from") != null
      ? (String) message.getOptions().get("from")
      : this.mailConnectionConfig.getSenderMail();

    this.validate(fromEmail, message);

    String textContent = message.getOptions() != null &&
      message.getOptions().get("text") != null
      ? (String) message.getOptions().get("text")
      : message.getBody();

    String htmlContent = message.getOptions() != null &&
      message.getOptions().get("html") != null
      ? (String) message.getOptions().get("html")
      : "";
    try {
      if (this.mailConnectionConfig.shouldSendToMultipleReceivers()) {
        this.sendToReceiversCombine(
            fromEmail,
            textContent,
            htmlContent,
            message
          );
      } else {
        this.sendToEachReceiverSeperately(
            fromEmail,
            textContent,
            htmlContent,
            message
          );
      }
    } catch (MailException e) {
      e.printStackTrace();
    }
  }

  private void validate(String fromEmail, Message message) {
    if (fromEmail == null || fromEmail.isBlank()) {
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.SENDER_NOT_FOUND.toString()
      );
    }

    if (message.getReceiver().getTo().size() == 0) {
      throw new HttpServerErrorException(
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
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.MESSAGE_DATA_NOT_FOUND.toString()
      );
    }
  }

  private void sendToReceiversCombine(
    String fromEmail,
    String textContent,
    String htmlContent,
    Message message
  ) {
    String[] receivers = message
      .getReceiver()
      .getTo()
      .stream()
      .map((Subscriber to) -> to.getId())
      .toArray(String[]::new);
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(fromEmail);
    simpleMailMessage.setTo(receivers);
    simpleMailMessage.setSubject(message.getSubject());
    simpleMailMessage.setText(message.getBody());

    mailConnectionConfig.getJavaMailSender().send(simpleMailMessage);
  }

  private void sendToEachReceiverSeperately(
    String fromEmail,
    String textContent,
    String htmlContent,
    Message message
  ) {
    for (Subscriber to : message.getReceiver().getTo()) {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom(fromEmail);
      simpleMailMessage.setTo(to.getId());
      simpleMailMessage.setSubject(message.getSubject());
      simpleMailMessage.setText(message.getBody());

      mailConnectionConfig.getJavaMailSender().send(simpleMailMessage);
    }
  }
}
