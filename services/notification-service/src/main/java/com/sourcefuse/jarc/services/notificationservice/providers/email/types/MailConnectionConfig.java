package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import org.springframework.mail.javamail.JavaMailSender;

public interface MailConnectionConfig {
  JavaMailSender getJavaMailSender();

  String getSenderMail();

  boolean shouldSendToMultipleReceivers();
}
