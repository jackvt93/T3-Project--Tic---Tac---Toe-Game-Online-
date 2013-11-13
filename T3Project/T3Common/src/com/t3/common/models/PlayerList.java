package com.t3.common.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class PlayerList implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int MAX_PLAYER = 2;
	
	/**
	 * The board owner name for determine
	 */
	private HashMap<String, Player> playerList;
	private int numPlayer;
	
	public PlayerList() {
		playerList = new HashMap<String, Player>();
		numPlayer = 0;
	}

	public HashMap<String, Player> getPlayerList() {
		return playerList;
	}
	
	public boolean addPlayer(User user) {
		String name = user.getName();
		
		if(this.playerList.containsKey(name)) {
			return false;
		}
		
		Player player = new Player(user, Player.PLAYER_STATE_NOT_READY_YET);
		this.playerList.put(name, player);
		this.numPlayer++;
		return true;
	}
	
	public boolean removePlayer(String name) {
		if(this.playerList.remove(name) == null) {
			return false;
		}
		this.playerList.remove(name);
		this.numPlayer--;
		return true;
	}
	
	public Player getPlayer(String name) {
		return playerList.get(name);
	}
	
	public Vector<Player> getPlayers() {
		return new Vector<>(playerList.values());
	}
	
	public boolean isFull() {
		return this.numPlayer == MAX_PLAYER;
	}
	
	public int getNumPlayer() {
		return this.numPlayer;
	}
}
