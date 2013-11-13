package com.t3.server.models;

import java.util.HashMap;
import java.util.Vector;

import com.t3.common.models.User;

/**
 * This class contain a list of user logged in server
 * @author Luan Vu
 * @see User
 */
public class UserList {
	
	private HashMap<String, User> userList;
	
	public UserList() {
		userList = new HashMap<String, User>();
	}

	public HashMap<String, User> getUserList() {
		return userList;
	}

	public void add(User user) {
		userList.put(user.getName(), user);
	}
	
	public void remove(String username) {
		userList.remove(username);
	}
	
	public boolean contains(String username) {
		return userList.containsKey(username);
	}
	
	public User getUser(String username) {
		return userList.get(username);
	}
	
	public Vector<User> getAllUser() {
		return new Vector<User>(userList.values());
	}
	
	public int size() {
		return userList.size();
	}
}
