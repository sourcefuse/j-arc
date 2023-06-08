package com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.twilio.types.TwilioSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@Service
@ConditionalOnProperty(value = "notification.provider.sms", havingValue = "TwilioProvider")
public class TwilioProvider implements SmsNotification {
	@Autowired
	TwilioConnectionConfig twilioConnection;

	@SuppressWarnings("unchecked")
	@Override
	public void publish(Message message) {

		if (message.getReceiver().getTo().size() == 0) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Message receiver not found in request");
		}

		for (Subscriber receiver : message.getReceiver().getTo()) {
			String from = twilioConnection.getSmsFrom();
			String to = "+" + receiver.getId();
			if (receiver.getType() != null
					&& receiver.getType().toString().equals(TwilioSubscriberType.TextSMSUser.toString())) {
				from = twilioConnection.getWhatsappFrom();
				to = "whatsapp:+" + receiver.getId();
			}

			MessageCreator messageCreator = new MessageCreator(new PhoneNumber(to), new PhoneNumber(from),
					message.getBody());

			if (message.getOptions().get("mediaUrl") != null) {
				messageCreator.setMediaUrl((List<URI>) message.getOptions().get("mediaUrl"));
			}
			if (receiver.getType() != null
					&& receiver.getType().toString().equals(TwilioSubscriberType.TextSMSUser.toString())
					&& twilioConnection.getSmsStatusCallback() != null) {
				messageCreator.setStatusCallback(twilioConnection.getSmsStatusCallback());
			}
			if (receiver.getType() == null && twilioConnection.getWhatsappStatusCallback() != null) {
				messageCreator.setStatusCallback(twilioConnection.getWhatsappStatusCallback());
			}
			messageCreator.create();
		}

	}

}
