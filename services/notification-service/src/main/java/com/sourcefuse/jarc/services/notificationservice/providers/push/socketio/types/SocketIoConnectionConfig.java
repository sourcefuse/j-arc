package com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.types;

import io.socket.client.Socket;

public interface SocketIoConnectionConfig {
  Socket getSocket();

  String getDefaultPath();
}
