package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

public interface SESConnectionConfig {
	public JavaMailSender getJavaMailSender();
	
	public MailSender getMailSender();

	public String getSenderMail();

	public Boolean shouldSendToMultipleReceivers();
}
