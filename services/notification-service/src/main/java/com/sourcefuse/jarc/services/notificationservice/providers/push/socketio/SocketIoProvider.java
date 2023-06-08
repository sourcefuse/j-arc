package com.sourcefuse.jarc.services.notificationservice.providers.push.socketio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.types.SocketConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(value = "notification.provider.push", havingValue = "SocketIoProvider")
public class SocketIoProvider implements PushNotification {

	@Autowired
	SocketConnectionConfig socketConnection;

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void publish(Message message) {
		if (socketConnection.getDefaultPath()!=null && socketConnection.getDefaultPath().isBlank()) {
			throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED,
					NotificationError.CHANNEL_INFO_MISSING.toString());
		}
		String event = message.getOptions() != null && message.getOptions().get("path") != null
				? (String) message.getOptions().get("path")
				: socketConnection.getDefaultPath();
		try {
			socketConnection.getSocket().emit(event, objectMapper.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
		}

	}

}
