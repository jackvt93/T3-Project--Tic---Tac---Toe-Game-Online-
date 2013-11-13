package com.t3.common.models;

import java.io.Serializable;

/**
 * This class is an instance user when user being used in server
 * @author Luan Vu
 */
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int rating, wins, loses, draws;

	public User(String name, int rating, int wins, int loses, int draws) {
		super();
		this.name = name;
		this.rating = rating;
		this.wins = wins;
		this.loses = loses;
		this.draws = draws;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
