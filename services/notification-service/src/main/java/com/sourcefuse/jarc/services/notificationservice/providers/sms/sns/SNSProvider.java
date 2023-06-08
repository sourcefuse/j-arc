package com.sourcefuse.jarc.services.notificationservice.providers.sms.sns;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SNSConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SNSSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;

import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishRequest.Builder;

@Service
@ConditionalOnProperty(value = "notification.provider.sms", havingValue = "SNSProvider")
public class SNSProvider implements SmsNotification {

	@Autowired
	SNSConnectionConfig snsConnection;

	@Override
	public void publish(Message message) {

		if (message.getReceiver().getTo().size() == 0) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Message receiver not found in request");
		}

		for (Subscriber receiver : message.getReceiver().getTo()) {

			Builder msgBuilder = PublishRequest.builder();
			msgBuilder.message(message.getBody());
			msgBuilder.subject(message.getSubject());

			if (message.getOptions().get("smsType") != null) {
				Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
				messageAttributes.put("AWS.SNS.SMS.SMSType", MessageAttributeValue.builder().dataType("String")
						.stringValue(message.getOptions().get("smsType").toString()).build());
				msgBuilder.messageAttributes(messageAttributes);

			}
			if (receiver.getType() != null
					&& receiver.getType().toString().equals(SNSSubscriberType.PhoneNumber.toString())) {
				msgBuilder.phoneNumber(receiver.getId());
			} else if (receiver.getType() != null
					&& receiver.getType().toString().equals(SNSSubscriberType.Topic.toString())) {
				msgBuilder.topicArn(receiver.getId());
			} else {
				// Do nothing
			}

			snsConnection.getSnsClient().publish(msgBuilder.build());
		}
	}

}
