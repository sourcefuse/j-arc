package com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.pubnub.api.PubNubException;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubPayloadType;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.types.Config;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(value = "notification.provider.push", havingValue = "PubNubProvider")
public class PubNubProvider implements PubNubNotification {

	@Autowired
	PubNubConnectionConfig pubnubConnection;

	private Map<String, Object> getGeneralMessageObject(Message message) {
		Map<String, Object> commonDataNotification = new HashMap<>();
		commonDataNotification.put("title", message.getSubject());
		commonDataNotification.put("description", message.getBody());

		Map<String, Object> pnGcm = new HashMap<>();
		if (message.getOptions().get("payloadType") != null
				&& message.getOptions().get("payloadType").toString().equals(PubNubPayloadType.Data.toString())) {
			pnGcm.put("data", commonDataNotification);
		} else {
			pnGcm.put("notification", commonDataNotification);
		}

		Map<String, Object> apsData = new HashMap<>();
		apsData.put("alert", commonDataNotification);
		apsData.put("key", message.getSubject());
		apsData.put("sound", message.getOptions().get("sound") != null ? message.getOptions().get("sound") : "default");

		Map<String, Object> targetTypeData = new HashMap<>();
		Map<String, Object> target = new HashMap<>();

		target.put("environment", pubnubConnection.getPubNubApns2Env());
		target.put("topic", pubnubConnection.getPubNubApns2BundleId());

		targetTypeData.put("targets", Collections.singletonList(target));
		targetTypeData.put("version", "v2");

		Map<String, Object> pnApns = new HashMap<>();
		pnApns.put("aps", apsData);
		pnApns.put("pnPush", Collections.singletonList(targetTypeData));

		Map<String, Object> result = new HashMap<>();
		result.put("pnGcm", pnGcm);
		result.put("pnApns", pnApns);

		return result;
	}

	@Override
	public void publish(Message message) {
		if (message.getReceiver().getTo().size() == 0) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,
					NotificationError.RECEIVERS_NOT_FOUND.toString());
		}
		if (message.getOptions() == null) {
			message.setOptions(new HashMap<String,Object>());
		}
		Map<String, Object> generalMessageObj = getGeneralMessageObject(message);
		Map<String, Object> pubnubMessage = new HashMap<>();

		pubnubMessage.putAll(generalMessageObj);
		pubnubMessage.putAll(Map.of("title", message.getSubject(), "description", message.getBody()));

		for (Subscriber receiver : message.getReceiver().getTo()) {
			String channel = "";
			if (receiver.getType() != null
					&& receiver.getType().toString().equals(PubNubSubscriberType.Channel.toString())) {
				channel = receiver.getId();
			}
			try {
				pubnubConnection.getPubNub().publish().message(pubnubMessage).channel(channel).sync();
			} catch (PubNubException e) {
				log.error(e.getMessage(), e);
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
			}

		}
	}

	@Override
	public Object grantAccess(Config config) {
		if (config.getOptions().get("token") != null && config.getOptions().get("ttl") != null) {
			Map<String, Object> result = new HashMap<>();
			try {
				pubnubConnection.getPubNub().grant()
						.authKeys(Collections.singletonList(config.getOptions().get("token").toString()))
						.channels(config.getReceiver().getTo().stream().map(item -> item.getId()).toList())
						.read(((boolean) config.getOptions().get("allowRead")) || true)
						.write(((boolean) config.getOptions().get("allowWrite")) || false)
						.ttl(Integer.valueOf(config.getOptions().get("ttl").toString())).sync();
			} catch (NumberFormatException | PubNubException e) {
				log.error(e.getMessage(), e);
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
			}
			result.put("ttl", config.getOptions().get("ttl"));
			return result;
		}
		throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Authorization token or ttl not found in request");
	}

	@Override
	public Object revokeAccess(Config config) {
		if (config.getOptions().get("token") != null) {
			Map<String, Object> result = new HashMap<>();
			try {
				pubnubConnection.getPubNub().grant()
						.authKeys(Collections.singletonList(config.getOptions().get("token").toString()))
						.channels(config.getReceiver().getTo().stream().map(item -> item.getId()).toList()).read(false)
						.write(false).sync();
			} catch (NumberFormatException | PubNubException e) {
				log.error(e.getMessage(), e);
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
			}
			result.put("ttl", config.getOptions().get("ttl"));
			return result;
		}
		throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Authorization token not found in request");
	}
}
