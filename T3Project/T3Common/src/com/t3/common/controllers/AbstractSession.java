package com.t3.common.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Abstract Session extends a Thread and Store Socket to the client, 
 * and a ObjectInputStream and ObjectOutputStream which can receive/send 
 * Object to user/server. This class also contains user name of the client 
 * @author Luan Vu
 *
 */
public abstract class AbstractSession extends Thread {
	
	private Socket socket;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private boolean running = true;

	/**
	 * Note: name of user !!! not userId of account !!!!
	 */
	protected String username;

	public AbstractSession(Socket socket) throws IOException {
		setSocket(socket);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		getStream();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void run() {
		try {
			while(running) {
				Object message = in.readObject();
				if (message != null) {
					onProcess(message);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onClean();
	}
	
	private void getStream() throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream()); 
		in = new ObjectInputStream(socket.getInputStream());
	}


	public void send(Object data) throws IOException {
		/* 
		 * ObjectOutputStream doesn't know about the 
		 * new contents so it just writes a back-reference 
		 * The ObjectInputStream at the other end sees the 
		 * back-reference and returns the same object it read last time, i.e. the original data.
		 * 				Stack overflow
		 * */
		out.reset();
		out.writeObject(data);
		out.flush();
	}
	
	public boolean isRunning() {
		return running;
	}

	public void stopRun() {
		this.running = false;
	}
	
	public abstract void onProcess(Object message);
	public abstract void onClean();
}
