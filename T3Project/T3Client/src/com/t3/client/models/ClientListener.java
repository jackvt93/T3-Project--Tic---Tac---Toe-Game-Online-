package com.t3.client.models;

/**
 * This interface using to communicate between client and 
 * SessionCommunicate (which interface with server session)
 * 
 * @author Luan Vu
 * @see SessionCommunicate
 */
public interface ClientListener {
	
	/**
	 * Receive a message specific to a panel 
	 * @param message is message from server
	 */
	public void onReceiveMessage(Object message);
}
