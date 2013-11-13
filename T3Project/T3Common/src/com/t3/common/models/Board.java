package com.t3.common.models;

import java.io.Serializable;

import com.t3.common.utils.Log;

/**
 * This class Contains board info
 * @author Luan Vu
 * 
 */
public class Board implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TAG = Board.class.getName();

	public static final int STATE_STARTED 		= 0x00000002;
	public static final int STATE_WAITING		= 0x00000003;

	public static final int JOIN_FULL 		= 0x00000004;
	public static final int JOIN_FAIL 		= 0x00000005;
	public static final int JOIN_SUCCESS 	 = 0x00000006;
	public static final int JOIN_PASSW_WRONG = 0x00000007;

	public static final int START_OK		= 0x00000008;
	public static final int START_NOT_ENOUGH = 0x00000009;
	public static final int START_NOT_READY = 0x0000000A;
	public static final int START_FAIL		= 0x0000000B;

	public static final int TICK_WIN		= 0x0000000C;
	public static final int TICK_DRAW		= 0x0000000D;
	public static final int TICK_CONT		= 0x0000000E;

	/** board number */
	private int boardNumber;

	/** board title and password */
	private String title;
	private char[] password;

	/** PlayerList contains list of players join and creator */
	private PlayerList playerList;

	/** The name's owner of board */
	private String ownerName;

	/** State of board */
	private int state;

	/** cells of board */
	private char[][] cell = new char[3][3];

	/** turn belong to who player ?*/
	private String currentTurn;

	/** Default constructor */
	public Board(int boardNumber, String title, char[] password, User creator) {
		this.boardNumber = boardNumber;
		this.title = title;
		this.password = password;
		this.ownerName = creator.getName();
		this.playerList = new PlayerList();
		this.state = STATE_WAITING;
		this.playerList.addPlayer(creator);
	}

	public Player getOwner() {
		return playerList.getPlayer(ownerName);
	}

	public Player getNotOwner() {
		for (Player player : playerList.getPlayers()) {
			if (!player.getName().equals(ownerName)) {
				return player;
			}
		}
		return null;
	}

	private void initCells() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cell[i][j] = ' ';
			}
		}
	}

	public int getBoardNumber() {
		return boardNumber;
	}

	public String getTitle() {
		return title;
	}

	public char[] getPassword() {
		return password;
	}

	public PlayerList getPlayerList() {
		return playerList;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getNotOwnerName() {
		return getNotOwner().getUser().getName();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(String name) {
		Log.console(TAG + " " + getBoardNumber(), "Current Turn: " + currentTurn); 
		this.currentTurn = name;
	}

	/**
	 * method using for tick into board 
	 * @return true if the player has win, false is continue game (no player win)
	 * @throws Exception 
	 */
	public int tick(int row, int column) throws Exception {
		if (cell[row][column] == ' ') {
			cell[row][column] = getCurrentTurnToken();
			if(isWon(getCurrentTurnToken())) {
				setState(STATE_WAITING);
				getNotOwner().setState(Player.PLAYER_STATE_NOT_READY_YET);
				return TICK_WIN;
			} else if(isFullToken()) {
				setState(STATE_WAITING);
				getNotOwner().setState(Player.PLAYER_STATE_NOT_READY_YET);
				return TICK_DRAW;
			}
			nextTurn();
		}
		return TICK_CONT;
	}

	public String getNextTurn() {
		for (Player player : playerList.getPlayers()) {
			if(player.getName() != currentTurn) {
				return player.getName();
			}
		}
		return null;
	}

	/** method using for set next turn */
	private void nextTurn() {
		setCurrentTurn(getNextTurn());
	}

	/** method using for check who has win
	 *  | X | X | X |		|	|	|	|		|	|	|	|
	 *  |   |   |   |		| X	| X	| X	|		|	|	|	|
	 *  |   |   |   |		|	|	|	|		| X	| X	| X	|
	 *  
	 *  | X |	|  	|		|  	| X	|  	|		|	|	| X	|
	 *  | X	|	|	|		| 	| X	|	|		|	|	| X	|
	 *  | X	|	|	|		| 	| X	|	|		|	|	| X	|
	 *  
	 *  | X	|	|  	|		|  	| 	| X |		
	 *  | 	| X	|	|		| 	| X |	|		
	 *  | 	|	| X	|		| X	| 	|	|		
	 *  
	 *  */
	public boolean isWon(char token) {
		/** Check all rows */
		for(int i = 0; i < 3; i++)
			if((cell[i][0] == token) 
					&& (cell[i][1] == token) 
					&& (cell[i][2] == token)) {
				return true;
			}

		/** Check all columns */
		for(int j = 0; j < 3; j++)
			if((cell[0][j] == token) 
					&& (cell[1][j] == token) 
					&& (cell[2][j] == token)) {
				return true;
			}

		/** Check major diagonal */
		if((cell[0][0] == token) 
				&& (cell[1][1] == token)
				&& (cell[2][2] == token)) {
			return true;
		}

		/** Check subdiagonal */
		if((cell[0][2] == token)
				&& (cell[1][1] == token)
				&& (cell[2][0] == token)) {
			return true;
		}
		return false;
	}

	public boolean isFullToken() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if(cell[i][j] == ' ')
					return false;
		return true;
	}

	/**
	 * This method is template not perfect
	 */
	public boolean isEnoughPlayer() {
		return isFullPlayer();
	}

	/**
	 * This method using for check all player ready or no
	 * @return true if all player in room is ready to play, false is players not ready
	 */
	public boolean isAllReady() {
		for (Player player : playerList.getPlayers()) {
			if (player.getName().equals(this.ownerName)) continue;
			if (player.getState() == Player.PLAYER_STATE_NOT_READY_YET)
				return false;
		}
		return true;
	}

	public int joinBoard(User user, char[] password) {
		if(new String(password).equals(new String(this.password))) {
			if(isFullPlayer()) {
				return JOIN_FULL;
			} else {
				return playerList.addPlayer(user) ? JOIN_SUCCESS : JOIN_FAIL;
			}
		} 
		return JOIN_PASSW_WRONG;
	}

	public int joinBoard(User user) {
		if(isFullPlayer())
			return JOIN_FULL;
		return playerList.addPlayer(user) ? JOIN_SUCCESS : JOIN_FAIL;
	}

	public void leaveRoom(String name) {
		playerList.removePlayer(name);
	}

	public boolean isFullPlayer() {
		return playerList.isFull();
	}

	public char getCurrentTurnToken() {
		return playerList.getPlayer(currentTurn).getToken();
	}

	public int startGame(char ownerToken) {
		if(getState() == STATE_STARTED ) {
			return START_FAIL;
		}
		if (!isAllReady()) {
			return START_NOT_READY;
		}
		if (!isFullPlayer()) {
			return START_NOT_ENOUGH;
		}
		Log.console("Board" + " " + getBoardNumber(), getTitle() + " start !");
		initCells();
		getOwner().setToken(ownerToken);
		getOwner().start();
		getNotOwner().setToken(ownerToken == 'X' ? 'O' : 'X'); 
		getNotOwner().start();
		setCurrentTurn(ownerName); 
		setState(STATE_STARTED);
		return START_OK;
	}

}
