package com.t3.server.models;

import java.util.HashMap;
import java.util.Vector;

import com.t3.server.controllers.Session;

/**
 * @author Luan Vu
 *
 */
public class SessionList {
	
	private HashMap<String, Session> sessionList;
	
	public SessionList() {
		this.sessionList = new HashMap<String, Session>();
	}
	
	public void add(String username, Session session) {
		this.sessionList.put(username, session);
	}
	
	public void remove(String username) {
		this.sessionList.remove(username);
	}
	
	public Session getSession(String username) {
		return this.sessionList.get(username);
	}
	
	public Vector<Session> getSessionList(){
		 return new Vector<>(sessionList.values());
	}
	
	public int size() {
		return this.sessionList.size();
	}	

}
