package com.sourcefuse.jarc.services.notificationservice.providers.push.socketio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.types.SocketIoConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@Slf4j
@ConditionalOnProperty(
  value = "notification.provider.push",
  havingValue = "SocketIoProvider"
)
public class SocketIoProvider implements PushNotification {

  @Autowired
  SocketIoConnectionConfig socketConnection;

  ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void publish(Message message) {
    if (message.getReceiver().getTo().size() == 0) {
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_NOT_FOUND.toString()
      );
    }
    if (
      socketConnection.getDefaultPath() == null ||
      socketConnection.getDefaultPath().isBlank()
    ) {
      throw new HttpServerErrorException(
        HttpStatus.PRECONDITION_FAILED,
        NotificationError.CHANNEL_INFO_MISSING.toString()
      );
    }
    String event = message.getOptions() != null &&
      message.getOptions().get("path") != null
      ? (String) message.getOptions().get("path")
      : socketConnection.getDefaultPath();
    try {
      socketConnection
        .getSocket()
        .emit(event, objectMapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      throw new HttpServerErrorException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Something went wrong"
      );
    }
  }
}
