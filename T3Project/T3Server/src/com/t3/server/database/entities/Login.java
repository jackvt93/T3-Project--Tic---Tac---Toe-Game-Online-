package com.t3.server.database.entities;

import java.sql.Date;

public class Login {
	private String username;
	private String password;
	private Date lastConnDate;
	private String lastIP;
	private int aid;
	
	public Login() {
	}

	public Login(String username, String password, Date lastConnDate,
			String lastIP, int aid) {
		super();
		this.username = username;
		this.password = password;
		this.lastConnDate = lastConnDate;
		this.lastIP = lastIP;
		this.aid = aid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Date getLastConnDate() {
		return lastConnDate;
	}

	public String getLastIP() {
		return lastIP;
	}

	public int getAid() {
		return aid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLastConnDate(Date lastConnDate) {
		this.lastConnDate = lastConnDate;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}
	
}
