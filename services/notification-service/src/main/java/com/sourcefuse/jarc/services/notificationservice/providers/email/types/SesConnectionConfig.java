package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

public interface SesConnectionConfig {
  JavaMailSender getJavaMailSender();

  MailSender getMailSender();

  String getSenderMail();

  boolean shouldSendToMultipleReceivers();
}
