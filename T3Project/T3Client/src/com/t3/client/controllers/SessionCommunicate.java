package com.t3.client.controllers;

import java.io.IOException;
import java.net.Socket;

import com.t3.client.models.ClientListener;
import com.t3.common.controllers.AbstractSession;

/**
 * This class receive/sends communication from/to the server
 * @author Luan Vu
 *
 */
public class SessionCommunicate extends AbstractSession {
	//private int id;
	private ClientListener clientListener;
	

	public SessionCommunicate(Socket socket, ClientListener clientInterface) throws IOException {
		super(socket);
		this.clientListener = clientInterface;
	}

	public void setClientListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}
	
	@Override
	public void onProcess(Object message) {
		if (message == null) {
			return;
		}
		clientListener.onReceiveMessage(message);
	}


	@Override
	public void onClean() {
	}
	
	@Override
	public void send(Object data) throws IOException {
		super.send(data);
	}
	

}
