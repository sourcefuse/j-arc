package com.sourcefuse.jarc.services.notificationservice.providers.push.socketio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.types.SocketIoConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@ConditionalOnProperty(
  value = "notification.provider.push",
  havingValue = "SocketIoProvider"
)
@RequiredArgsConstructor
public class SocketIoProvider implements PushNotification {

  private static final String PATH_KEY = "path";

  private final SocketIoConnectionConfig socketConnection;

  @Override
  public void publish(Message message) {
    if (message.getReceiver().getTo().isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_NOT_FOUND.toString()
      );
    }
    if (
      socketConnection.getDefaultPath() == null ||
      socketConnection.getDefaultPath().isBlank()
    ) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        NotificationError.CHANNEL_INFO_MISSING.toString()
      );
    }
    String event = message.getOptions() != null &&
      message.getOptions().get(PATH_KEY) != null
      ? (String) message.getOptions().get(PATH_KEY)
      : socketConnection.getDefaultPath();
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      socketConnection
        .getSocket()
        .emit(event, objectMapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        NotificationError.SOMETHING_WNET_WRONG.toString()
      );
    }
  }
}
