package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.email.javamailer.JavaMailerProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.MailConnectionConfig;

import jakarta.mail.internet.MimeMessage;

class JavaMailerProviderTests {
	@Mock
	private MailConnectionConfig mailConnectionConfig;

	@Mock
	private JavaMailSender javaMailSender;

	@InjectMocks
	private JavaMailerProvider javaMailerProvider;

	Notification message;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		MimeMessage mockMimeMessage = mock(MimeMessage.class);
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
		Mockito.when(mailConnectionConfig.getJavaMailSender()).thenReturn(javaMailSender);
		Mockito.when(mailConnectionConfig.getSenderMail()).thenReturn("abc@example.com");
		Mockito.when(mailConnectionConfig.shouldSendToMultipleReceivers()).thenReturn(true);

		message = MockNotifications.getEmailNotificationObj();
	}

	/**
	 * successfully sends single mail to all receiver
	 */
	@Test
	public void testPublish_SendsMailToReceivers() {
		javaMailerProvider.publish(message);

		Mockito.verify(javaMailSender, Mockito.times(1)).send(Mockito.any(MimeMessage.class));
	}

	/**
	 * successfully send multiple mails to each receiver seperately
	 */
	@Test
	public void testPublish_SendsMultipleMailToReceivers() {
		//set to send mail to each user seperately 
		Mockito.when(mailConnectionConfig.shouldSendToMultipleReceivers()).thenReturn(false);

		javaMailerProvider.publish(message);

		Mockito.verify(javaMailSender, Mockito.times(2)).send(Mockito.any(MimeMessage.class));
	}

	/**
	 * Fail to execute due to receivers are empty
	 */
	@Test
	public void testPublish_FailDuetoEmptyReceivers() {
		message.getReceiver().setTo(Arrays.asList());

		HttpServerErrorException exception = assertThrows(HttpServerErrorException.class,
				() -> javaMailerProvider.publish(message));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
		assertEquals(NotificationError.RECEIVERS_NOT_FOUND.toString(), exception.getStatusText());
	}

	/**
	 * Fail to execute due to sender mail is empty 
	 */
	@Test
	public void testPublish_FailDuetoEmptyFromMail() {
		// set to from mail to empty
		Mockito.when(mailConnectionConfig.getSenderMail()).thenReturn(null);
		
		HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> javaMailerProvider.publish(message));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
		assertEquals(NotificationError.SENDER_NOT_FOUND.toString(), exception.getStatusText());

	}

	/**
	 * Fail to execute due to subject of mail is empty
	 */
	@Test
	public void testPublish_FailDuetoEmptySubject() {
		message.setSubject(null);

		HttpServerErrorException exception = assertThrows(HttpServerErrorException.class,
				() -> javaMailerProvider.publish(message));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
		assertEquals(NotificationError.MESSAGE_DATA_NOT_FOUND.toString(), exception.getStatusText());

	}

	/**
	 * Fail to execute due to body of mail is empty
	 */
	@Test
	public void testPublish_FailDuetoEmptyBody() {
		message.setSubject(null);

		HttpServerErrorException exception = assertThrows(HttpServerErrorException.class,
				() -> javaMailerProvider.publish(message));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
		assertEquals(NotificationError.MESSAGE_DATA_NOT_FOUND.toString(), exception.getStatusText());

	}
}
