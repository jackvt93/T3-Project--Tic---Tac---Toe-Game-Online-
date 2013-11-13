package com.t3.server.controllers;

import java.io.IOException;
import java.net.Socket;

import com.t3.common.controllers.AbstractSession;
import com.t3.common.models.Board;
import com.t3.common.models.Bundle;
import com.t3.common.models.Package;
import com.t3.common.models.PackageKind;
import com.t3.common.models.User;
import com.t3.common.utils.Log;
import com.t3.server.database.businesslogic.LoginHelper;
import com.t3.server.database.businesslogic.UserHelper;
import com.t3.server.models.AccountList;
import com.t3.server.models.BoardList;
import com.t3.server.models.SessionList;
import com.t3.server.models.UserList;
import com.t3.server.views.T3Server;


/**
 * This class is a Session of one client when client connect success.</br>
 * Class process actions client request and response </br>
 * This class also send to broadcast, sendtoUser of another session 
 * @author Luan Vu
 *
 */
public class Session extends AbstractSession {

	private static final String TAG = Session.class.getName();

	private String userId;

	private T3Server server;
	private UserList userList;
	private BoardList boardList;
	private AccountList accountList;
	private SessionList sessionList;

	public Session(Socket socket) throws IOException {
		super(socket);
		this.server = T3Server.getInstance();
		this.sessionList = server.getSessionList();
		this.boardList = server.getBoardList();
		this.userList = server.getUserList();
		this.accountList = server.getAccountList();
	}

	@Override
	public void onProcess(Object message){
		Package pack = (Package)message;
		processPackage(pack);

	}

	@Override
	public void onClean() {
		
	}

	/*	private void sendBroadcast(Package pack) throws IOException {
		Vector<User> users = userList.getAllUser();
		for (User user : users) {
			String currentName = user.getName();
			sendToUser(currentName, pack); 

		}
	}*/

	private void sendToUser(String username, Package pack) throws IOException {
		Session session = sessionList.getSession(username);
		session.send(pack);
	}

	private void sendBroadcastButNotMe(Package pack) throws IOException {
		for (User user : userList.getAllUser()) {
			String currentName = user.getName();
			if (currentName.equals(this.getUsername()))
				continue;
			sendToUser(currentName, pack);
		}
	}

	/**
	 * @param pack object need to proccess
	 * This extract method process transfer object receive from client
	 */
	private void processPackage(Package pack) {
		try {

			switch (pack.getKind()) {
			case PackageKind.ACTION_LOGIN:
			{
				LoginHelper loginHelper = new LoginHelper();

				/* accountID and password receive from client */
				String userId = (String)pack.getData();
				char[] password = (char[])pack.getExtras();

				/* accountId is a login result, if accountId > 0 client login success, -1 if login fail(not exist) */ 
				int accountId = loginHelper.login(userId, password);

				/* If user name and password valid and not logged on server (not contains in accountList) 
				 * then send result to client ---> login success
				 * And, if user name and password valid and this account logged on server(contains in accountList)
				 * then send result to client ---> user being used 
				 * Finally, if user name or password invalid (accountId = -1) then send to client ---> login fail */
				if(accountId > 0 && !accountList.contains(userId)) {		
					Log.console(TAG, "Account: " + userId + " logged in");
					UserHelper userHelper = new UserHelper();
					User commonUser = userHelper.getCommonUser(accountId);
					if (commonUser != null) {
						userList.add(commonUser);
						this.setUsername(commonUser.getName());
						this.sessionList.add(commonUser.getName(), this);
					}
					this.userId = userId;
					this.accountList.add(userId, accountId); 

					send(new Package(commonUser, PackageKind.RESULT_LOGIN_SUCCESS));
					sendBroadcastButNotMe(new Package(commonUser, true, PackageKind.ACTION_ROOM_UPDATE_USER_LIST));
				} else if (accountId > 0 && accountList.contains(userId)) {			
					Log.console(TAG, userId + " login fail (Account being used)");
					send(new Package(null, PackageKind.RESULT_LOGIN_USERID_USED));

				} else {																		
					Log.console(TAG, userId + " login fail (Account not true)");
					send(new Package(null, PackageKind.RESULT_LOGIN_USERID_FAIL));
				}
				break;
			}

			case PackageKind.ACTION_LOGOUT:
			{
				Log.console(TAG, this.username + " request log out");
				LoginHelper loginHelper = new LoginHelper();
				loginHelper.updateConnDate(this.userId);
				send(new Package(null, PackageKind.RESULT_LOGOUT_OK));
				sendBroadcastButNotMe(new Package(this.getUsername(), false, PackageKind.ACTION_ROOM_UPDATE_USER_LIST));
				this.accountList.remove(this.userId);
				this.sessionList.remove(this.getUsername());
				this.userList.remove(this.getUsername());
				this.stopRun();
				break;
			}

			case PackageKind.ACTION_CREATE_USER:
			{
				UserHelper userHelper = new UserHelper();
				String name = (String) pack.getData();
				String userId = (String)pack.getExtras();

				int accountID = accountList.getAccountId(userId);

				int nRet = userHelper.create(new com.t3.server.database.entities.User(name, accountID));
				if (nRet == UserHelper.CREATE_USER_SUCCESS) {
					User commonUser = userHelper.getCommonUser(accountID);
					userList.add(commonUser);
					this.setUsername(commonUser.getName());
					sessionList.add(commonUser.getName(), this);
					send(new Package(commonUser, PackageKind.RESULT_CREATE_USER_SUCCESS));
					sendBroadcastButNotMe(new Package(commonUser, true ,PackageKind.ACTION_ROOM_UPDATE_USER_LIST)); 
				} else if (nRet == UserHelper.CREATE_USER_EXISTED) {
					send(new Package(null, PackageKind.RESULT_CREATE_USER_EXISTED));
				} else if(nRet == UserHelper.CREATE_USER_FAIL) {
					send(new Package(null, PackageKind.RESULT_CREATE_USER_FAIL)); 
				}
				break;
			}

			case PackageKind.ACTION_GET_USER_LIST:
				Log.console(TAG, this.username + " request get user list");
				send(new Package(userList.getAllUser(), PackageKind.RESULT_GET_USER_LIST));
				break;

			case PackageKind.ACTION_GET_BOARD_LIST:
				Log.console(TAG, this.username + " request get table list");
				send(new Package(boardList.getAllBoard(), PackageKind.RESULT_GET_BOARD_LIST));
				break;

			case PackageKind.ACTION_CREATE_BOARD:
			{
				Log.console(TAG, this.username + " request create new board");
				User creator = (User)pack.getData();
				Bundle bundle = (Bundle)pack.getExtras();
				Board board = boardList.createBoard(creator, bundle.getString("title"), bundle.getCharArray("password"));
				if (board != null) {
					send(new Package(board, PackageKind.RESULT_CREATE_BOARD_SUCCESS));
					sendBroadcastButNotMe(new Package(board, true, PackageKind.ACTION_ROOM_UPDATE_BOARD_LIST)); 
				} else {
					send(new Package(board, PackageKind.RESULT_CREATE_BOARD_FAIL));
				}
				break;
			}

			/** 2013 - 9 - 11 : Nam */
			case PackageKind.ACTION_JOIN_BOARD:
			{
				try {
					Log.console(TAG, this.username + " request join board");

					int boardNumber = (int)pack.getData();
					Board board = boardList.getBoard(boardNumber);
					User user = (User)pack.getExtras();

					if(board.getPassword().length > 0) {
						send(new Package(board.getBoardNumber(), PackageKind.ACTION_INPUT_PASS_BOARD));
					} else {
						int joinResult = boardList.getBoard(boardNumber).joinBoard(user);
						if(joinResult == Board.JOIN_SUCCESS) {
							send(new Package(boardList.getBoard(boardNumber), PackageKind.RESULT_JOIN_BOARD_SUCCESS));
							sendToUser(board.getOwnerName(), new Package(board, user, PackageKind.ACTION_BOARD_UPDATE_DATA));  
						} else if (joinResult == Board.JOIN_FULL) {
							send(new Package(null, PackageKind.RESULT_JOIN_BOARD_FULL));
						} else if(joinResult == Board.JOIN_FAIL) {
							send(new Package(null, PackageKind.RESULT_JOIN_BOARD_FAIL));
						}
					}

				} catch (NullPointerException e) {
					send(new Package(null, PackageKind.RESULT_JOIN_BOARD_NOT_EXIST));
				}

				break;
			}

			/* 10-11-2013 Năm */
			case PackageKind.RESULT_PASSWORD_BOARD: {

				Log.console(TAG, this.username + " request join table by pass");
				User user = (User)pack.getData();
	/*			Board board2 = (Board) pack.getData();
				Board board3 = boardList.getBoard(board2.getBoardNumber());*/
				Bundle bundle = (Bundle)pack.getExtras();
				int boardNumber = bundle.getInt("boardNumber");
				char[] password = bundle.getCharArray("password");
				
				Board board = boardList.getBoard(boardNumber);
						
				int joinResult = board.joinBoard(user, password);
				
				if(joinResult == Board.JOIN_SUCCESS) {
					send(new Package(board, PackageKind.RESULT_JOIN_BOARD_SUCCESS));
					sendToUser(board.getOwnerName(), new Package(board, PackageKind.ACTION_BOARD_UPDATE_DATA) );
				} else if(joinResult == Board.JOIN_PASSW_WRONG) {
					send(new Package(null, PackageKind.RESULT_JOIN_BOARD_PASSW_WRONG));
				} else if (joinResult == Board.JOIN_FULL) {
					send(new Package(null, PackageKind.RESULT_JOIN_BOARD_FULL));
				} else if(joinResult == Board.JOIN_FAIL) {
					send(new Package(null, PackageKind.RESULT_JOIN_BOARD_FAIL));
				}
				break;
			}

			/** 2013 - 8 - 11 : Nam */
			case PackageKind.ACTION_SEND_ROOM_MESSAGE:
			{
				Bundle buddle = (Bundle) pack.getExtras();
				String s = buddle.getString("message");
				sendBroadcastButNotMe(new Package(s, PackageKind.RESULT_MESSAGE_ROOM));
				break;
			}

			case PackageKind.ACTION_BOARD_LEAVE: {
				Integer boardNumber = (Integer)pack.getData();
				String name = (String)pack.getExtras();

				Board board = boardList.getBoard(boardNumber);
				if(board.getOwnerName().equals(name)) {
					boardList.removeBoard(boardNumber);
					if(board.getNotOwner() != null)
						sendToUser(board.getNotOwnerName(), new Package(null, PackageKind.ACTION_BOARD_LEAVE));
					send(new Package(null, PackageKind.RESULT_BOARD_LEAVE_OK));
					sendBroadcastButNotMe(new Package(boardNumber, false, PackageKind.ACTION_ROOM_UPDATE_BOARD_LIST));
				} else {
					board.getPlayerList().removePlayer(name);
					send(new Package(null, PackageKind.RESULT_BOARD_LEAVE_OK));
					sendToUser(board.getOwnerName(), new Package(board, PackageKind.ACTION_BOARD_UPDATE_DATA));
				}

				break;
			}

			case PackageKind.ACTION_BOARD_START:
			{
				Log.console(TAG, this.username + " request start");

				int boardNumber = (int)pack.getData();
				Board board = boardList.getBoard(boardNumber);

				char ownerToken = (char)pack.getExtras();
				int startGameResult = board.startGame(ownerToken);

				if (startGameResult == Board.START_OK) {
					send(new Package(board, PackageKind.RESULT_BOARD_START_OK));
					sendToUser(board.getNotOwnerName(), new Package(board, PackageKind.RESULT_BOARD_START_OK));  
				} else if (startGameResult == Board.START_NOT_READY) {
					send(new Package(board, PackageKind.RESULT_BOARD_START_PLAYER_NOT_READY));
					sendToUser(board.getNotOwnerName(), new Package(board, PackageKind.RESULT_BOARD_START_PLAYER_NOT_READY));  
				} else if (startGameResult == Board.START_NOT_ENOUGH) {
					send(new Package(board, PackageKind.RESULT_BOARD_START_NOT_ENOUGH_PLAYER));
				} else {
					send(new Package(board, PackageKind.RESULT_BOARD_START_FAIL));
					sendToUser(board.getNotOwnerName(), new Package(board, PackageKind.RESULT_BOARD_START_FAIL));  
				}
				break;
			}

			case PackageKind.ACTION_BOARD_READY: {
				Log.console(TAG, this.username + " request ready");
				Integer boardNumber = (Integer)pack.getData();
				String name = (String)pack.getExtras();

				Board board = boardList.getBoard(boardNumber);
				board.getPlayerList().getPlayer(name).ready();

				send(new Package(board, PackageKind.RESULT_BOARD_READY_OK));
				sendToUser(board.getOwnerName(), new Package(board, PackageKind.ACTION_BOARD_UPDATE_DATA));
				break;
			}

			case PackageKind.ACTION_BOARD_UNREADY: {
				Log.console(TAG, this.username + " request unready");
				Integer boardNumber = (Integer)pack.getData();
				String name = (String)pack.getExtras();

				Board board = boardList.getBoard(boardNumber);
				board.getPlayerList().getPlayer(name).unready();

				send(new Package(board, PackageKind.RESULT_BOARD_UNREADY_OK));
				sendToUser(board.getOwnerName(), new Package(board, PackageKind.ACTION_BOARD_UPDATE_DATA));
				break;
			}

			case PackageKind.ACTION_BOARD_PLAYER_TICK: {
				Integer boardNumber = (Integer)pack.getData();
				Board board = boardList.getBoard(boardNumber);

				Bundle bundle = (Bundle)pack.getExtras();
				int row = bundle.getInt("row");
				int column = bundle.getInt("column");

				int tick = board.tick(row, column);

				if(tick == Board.TICK_CONT) {
					sendToUser(board.getCurrentTurn(), new Package(bundle, PackageKind.ACTION_BOARD_PLAYER_TICK));  
				} else if (tick == Board.TICK_DRAW) {
					User updateUser = board.getPlayerList().getPlayer(board.getNextTurn()).getUser();
					User updateUser2 = board.getPlayerList().getPlayer(board.getCurrentTurn()).getUser();
					updateUser.setDraws(updateUser.getDraws() + 1);
					updateUser2.setDraws(updateUser2.getDraws() + 1);
					UserHelper userHelper = new UserHelper();
					userHelper.update(updateUser);
					userHelper.update(updateUser2);

					sendToUser(board.getNextTurn(), new Package(bundle, PackageKind.ACTION_BOARD_PLAYER_TICK));  
					sendToUser(board.getNextTurn(), new Package(null, PackageKind.ACTION_BOARD_YOU_DRAW));  
					sendToUser(board.getCurrentTurn(), new Package(null, PackageKind.ACTION_BOARD_YOU_DRAW));  
				} else {
					User updateUser = board.getPlayerList().getPlayer(board.getNextTurn()).getUser();
					User updateUser2 = board.getPlayerList().getPlayer(board.getCurrentTurn()).getUser();
					updateUser.setLoses(updateUser.getLoses() + 1);
					updateUser2.setWins(updateUser2.getWins() + 1);
					updateUser2.setRating(updateUser2.getWins() / 100 + 1);
					UserHelper userHelper = new UserHelper();
					userHelper.update(updateUser);
					userHelper.update(updateUser2);

					send(new Package(null, PackageKind.ACTION_BOARD_YOU_WON));
					sendToUser(board.getNextTurn(), new Package(bundle, PackageKind.ACTION_BOARD_YOU_LOSE));  
				}

				break;
			}

			/* 10-11-2013 Năm */
			case PackageKind.ACTION_SEND_BOARD_MESSAGE:

				String ss = (String)pack.getData();		
				Integer boardNumber = (Integer)pack.getExtras();
				Board board = boardList.getBoard(boardNumber);

				/* if me not owner then send to owner and opposite */
				if(board.getOwnerName().equals(this.getUsername()) && board.getNotOwner() != null) {
					sendToUser(board.getNotOwnerName(), new Package(ss, PackageKind.RESULT_MESSAGE_BOARD)); 
				} else if(!board.getOwnerName().equals(this.getUsername())){
					sendToUser(board.getOwnerName(), new Package(ss, PackageKind.RESULT_MESSAGE_BOARD));
				}

				/* Warning: never send broadcast here !!!! (to Nam) */
				//sendBroadcastButNotMe(new Package(ss,boardNumber,PackageKind.RESULT_MESSAGE_BOARD));
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
