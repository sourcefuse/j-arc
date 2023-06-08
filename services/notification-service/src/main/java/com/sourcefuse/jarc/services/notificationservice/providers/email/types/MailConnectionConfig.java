package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import org.springframework.mail.javamail.JavaMailSender;

public interface MailConnectionConfig {
	public JavaMailSender getJavaMailSender();

	public String getSenderMail();

	public Boolean shouldSendToMultipleReceivers();
}
