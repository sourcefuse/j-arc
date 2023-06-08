package com.sourcefuse.jarc.services.notificationservice.providers.email.javamailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.EmailNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.MailConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@ConditionalOnProperty(value = "notification.provider.email", havingValue = "JavaMailerProvider")
public class JavaMailerProvider implements EmailNotification {
	@Autowired
	MailConnectionConfig mailConnectionConfig;

	@Override
	public void publish(Message message) {
		String fromEmail = message.getOptions() != null && message.getOptions().get("from") != null
				? (String) message.getOptions().get("from")
				: this.mailConnectionConfig.getSenderMail();

		this.validate(fromEmail, message);

		String textContent = message.getOptions() != null && message.getOptions().get("text") != null
				? (String) message.getOptions().get("text")
				: message.getBody();

		String htmlContent = message.getOptions() != null && message.getOptions().get("html") != null
				? (String) message.getOptions().get("html")
				: "";
		try {
			if (this.mailConnectionConfig.shouldSendToMultipleReceivers()) {
				this.sendToReceiversCombine(fromEmail, textContent, htmlContent, message);
			} else {
				this.sendToEachReceiverSeperately(fromEmail, textContent, htmlContent, message);
			}
		} catch (MessagingException e) {
			new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}
	}

	private void validate(String fromEmail, Message message) {
		if (fromEmail == null || fromEmail.isBlank()) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, NotificationError.SENDER_NOT_FOUND.toString());
		}

		if (message.getReceiver().getTo().size() == 0) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
					NotificationError.RECEIVERS_NOT_FOUND.toString());
		}
		if (message.getSubject() == null || message.getSubject().isBlank() || message.getBody() == null
				|| message.getBody().isBlank()) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
					NotificationError.MESSAGE_DATA_NOT_FOUND.toString());
		}
	}

	private void sendToReceiversCombine(String fromEmail, String textContent, String htmlContent, Message message)
			throws MessagingException {
		String[] receivers = message.getReceiver().getTo().stream().map((Subscriber to) -> to.getId())
				.toArray(String[]::new);

		MimeMessage mimeMessage = mailConnectionConfig.getJavaMailSender().createMimeMessage();
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

	private void sendToEachReceiverSeperately(String fromEmail, String textContent, String htmlContent, Message message)
			throws MessagingException {
		for (Subscriber to : message.getReceiver().getTo()) {
			MimeMessage mimeMessage = mailConnectionConfig.getJavaMailSender().createMimeMessage();
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
