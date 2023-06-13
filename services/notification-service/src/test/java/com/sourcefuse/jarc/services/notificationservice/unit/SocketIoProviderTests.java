package com.sourcefuse.jarc.services.notificationservice.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.SocketIoProvider;
import com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.types.SocketIoConnectionConfig;
import io.socket.client.Socket;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class SocketIoProviderTests {

  @Mock
  private SocketIoConnectionConfig socketIoConnectionConfig;

  @Mock
  private Socket socket;

  @InjectMocks
  private SocketIoProvider socketIoProvider;

  Notification message;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);

    Mockito.when(socketIoConnectionConfig.getSocket()).thenReturn(socket);
    Mockito
      .when(socketIoConnectionConfig.getDefaultPath())
      .thenReturn("Dummy-Topic");

    message = MockNotifications.getSocketIoNotificationObj();
  }

  /**
   * successfully sends push notification to all receiver
   */
  @Test
  void testPublish_SendsNotificationToReceivers() {
    socketIoProvider.publish(message);

    Mockito
      .verify(socket, Mockito.times(1))
      .emit(Mockito.any(String.class), Mockito.any(String.class));
  }

  /**
   * Fail to execute due to receivers are empty
   */
  @Test
  void testPublish_FailDuetoEmptyReceivers() {
    message.getReceiver().setTo(Arrays.asList());

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> socketIoProvider.publish(message)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals(
      NotificationError.RECEIVERS_NOT_FOUND.toString(),
      exception.getReason()
    );
  }

  /**
   * Fail to execute due to default path not found
   */
  @Test
  void testPublish_FailDuetoDefaultPath() {
    Mockito.when(socketIoConnectionConfig.getDefaultPath()).thenReturn(null);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> socketIoProvider.publish(message)
    );

    assertEquals(HttpStatus.PRECONDITION_FAILED, exception.getStatusCode());
    assertEquals(
      NotificationError.CHANNEL_INFO_MISSING.toString(),
      exception.getReason()
    );
  }
}
