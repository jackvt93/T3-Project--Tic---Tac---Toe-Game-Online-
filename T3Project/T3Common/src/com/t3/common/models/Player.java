package com.t3.common.models;

import java.io.Serializable;

import com.t3.common.utils.Log;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int PLAYER_STATE_STARTED = 0x0001;
	public static final int PLAYER_STATE_READY_TO_START = 0x0002;
	public static final int PLAYER_STATE_NOT_READY_YET = 0x0003;
	
	private User user = null;
	private int state;
	
	/** this var show that player play at X or O, true is X false is O*/
	private char token;
	
	public Player(User user, int state) {
		this.user = user;
		this.state = state;
	}
	
	/** player name that is user name */
	public String getName() {
		return this.user.getName();
	}

	public User getUser() {
		return user;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public char getToken() {
		return this.token;
	}
	
	public void setToken(char token) {
		this.token = token;
	}
	
	public void ready() {
		Log.console("Player", getName() + " ready");
		if (isPlaying() || isReady()) {
			return;
		}
		setState(PLAYER_STATE_READY_TO_START); 
	}
	
	public void unready() {
		if(isReady()) {
			setState(PLAYER_STATE_NOT_READY_YET); 
		}
	}
	
	public void start() {
		if(isReady()) {
			setState(PLAYER_STATE_STARTED); 
		}
	}
	
	public boolean isPlaying() {
		return state == PLAYER_STATE_STARTED;
	}
	
	public boolean isReady() {
		return state == PLAYER_STATE_READY_TO_START;
	}
	
	public boolean isNotReady() {
		return state == PLAYER_STATE_NOT_READY_YET;
	}
	
}
