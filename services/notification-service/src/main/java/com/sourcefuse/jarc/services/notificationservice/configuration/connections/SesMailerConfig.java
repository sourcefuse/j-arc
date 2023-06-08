package com.sourcefuse.jarc.services.notificationservice.configuration.connections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sourcefuse.jarc.services.notificationservice.providers.email.types.SESConnectionConfig;

import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Service
@ConditionalOnProperty(value = "notification.provider.email", havingValue = "SESProvider")
public class SesMailerConfig implements SESConnectionConfig {

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${notification.config.sender-mail:#{null}}")
	private String senderMail;

	@Value("${notification.config.send-to-multiple-receivers:#{false}}")
	private Boolean sendToMultipleReceivers;

	private final JavaMailSender javaMailSender;

	SesMailerConfig() {
		System.out.println("Hello");
		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

		SesClient client = SesClient.builder().region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
		try {
			javaMailSender = new SimpleEmailServiceJavaMailSender(client);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Hello " + e.getMessage());
			throw e;
		}
	}

	@Override
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	@Override
	public String getSenderMail() {
		return senderMail;
	}

	@Override
	public Boolean shouldSendToMultipleReceivers() {
		return sendToMultipleReceivers;
	}

	@Override
	public MailSender getMailSender() {
		// TODO Auto-generated method stub
		return null;
	}

}
