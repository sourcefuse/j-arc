package com.sourcefuse.jarc.services.notificationservice.providers.email.javamailer;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.EmailNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.MailConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@ConditionalOnProperty(
  value = "notification.provider.email",
  havingValue = "JavaMailerProvider"
)
@RequiredArgsConstructor
@Slf4j
public class JavaMailerProvider implements EmailNotification {

  private static final String FROM_KEY = "from";
  private static final String TEXT_KEY = "text";
  private static final String HTML_KEY = "html";

  private final MailConnectionConfig mailConnectionConfig;

  @Override
  public void publish(Message message) {
    String fromEmail = Optional
      .ofNullable(message.getOptions())
      .map(options -> (String) options.get(FROM_KEY))
      .orElse(this.mailConnectionConfig.getSenderMail());

    this.initialValidations(fromEmail, message);

    String textContent = Optional
      .ofNullable(message.getOptions())
      .map(options -> (String) options.get(TEXT_KEY))
      .orElse(message.getBody());

    String htmlContent = Optional
      .ofNullable(message.getOptions())
      .map(options -> (String) options.get(HTML_KEY))
      .orElse("");
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
    } catch (MessagingException e) {
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

  void sendToReceiversCombine(
    String fromEmail,
    String textContent,
    String htmlContent,
    Message message
  ) throws MessagingException {
    String[] receivers = message
      .getReceiver()
      .getTo()
      .stream()
      .map(Subscriber::getId)
      .toArray(String[]::new);

    MimeMessage mimeMessage = mailConnectionConfig
      .getJavaMailSender()
      .createMimeMessage();
    MimeMessageHelper helper;
    helper = new MimeMessageHelper(mimeMessage, true);
    helper.setFrom(fromEmail);
    helper.setTo(receivers);
    helper.setSubject(message.getSubject());
    if (htmlContent.isBlank()) {
      helper.setText(textContent, false);
    } else {
      helper.setText(textContent, htmlContent);
    }
    mailConnectionConfig.getJavaMailSender().send(mimeMessage);
  }

  void sendToEachReceiverSeperately(
    String fromEmail,
    String textContent,
    String htmlContent,
    Message message
  ) throws MessagingException {
    for (Subscriber to : message.getReceiver().getTo()) {
      MimeMessage mimeMessage = mailConnectionConfig
        .getJavaMailSender()
        .createMimeMessage();
      MimeMessageHelper helper;
      helper = new MimeMessageHelper(mimeMessage, true);
      helper.setFrom(fromEmail);
      helper.setTo(to.getId());
      helper.setSubject(message.getSubject());
      if (htmlContent.isBlank()) {
        helper.setText(textContent, false);
      } else {
        helper.setText(textContent, htmlContent);
      }
      mailConnectionConfig.getJavaMailSender().send(mimeMessage);
    }
  }
}
