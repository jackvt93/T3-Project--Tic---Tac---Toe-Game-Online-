package com.t3.server.database.entities;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int rating, wins, loses, draws, aid;

	public User() {
		
	}
	
	public User(String name, int aid) {
		this.name = name;
		this.aid = aid;
	}

	public User(String name, int rating, int wins, int loses, int draws, int aid) {
		super();
		this.name = name;
		this.rating = rating;
		this.wins = wins;
		this.loses = loses;
		this.draws = draws;
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public int getRating() {
		return rating;
	}

	public int getWins() {
		return wins;
	}

	public int getLoses() {
		return loses;
	}

	public int getDraws() {
		return draws;
	}

	public int getAid() {
		return aid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}
	
}