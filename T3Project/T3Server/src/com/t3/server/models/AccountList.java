package com.t3.server.models;

import java.util.HashMap;

public class AccountList {
	private HashMap<String, Integer> AccountList;
	
	public AccountList() {
		AccountList = new HashMap<String, Integer>();
	}
	
	public void add(String username, Integer accountId) {
		AccountList.put(username, accountId);
	}
	
	public void remove(String username) {
		AccountList.remove(username);
	}
	
	public boolean contains(int accountId) {
		return AccountList.containsValue(accountId);
	}
	
	public boolean contains(String username) {
		return AccountList.containsKey(username);
	}
	
	public int getAccountId(String username) {
		return AccountList.get(username);
	}
}
