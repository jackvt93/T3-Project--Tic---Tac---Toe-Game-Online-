package com.t3.server.database.entities;

public class Account {
	private int aid;
	private String name;
	private String email;
	private String sex;
	private String address;

	public Account() {
	}

	public Account(int aid, String name, String email, String sex,
			String address) {
		super();
		this.aid = aid;
		this.name = name;
		this.email = email;
		this.sex = sex;
		this.address = address;
	}

	public int getAid() {
		return aid;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getSex() {
		return sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
