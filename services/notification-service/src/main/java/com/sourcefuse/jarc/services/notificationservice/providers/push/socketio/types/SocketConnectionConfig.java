package com.sourcefuse.jarc.services.notificationservice.providers.push.socketio.types;

import io.socket.client.Socket;

public interface SocketConnectionConfig {

	public Socket getSocket();
	
	public String getDefaultPath();
}
