package com.t3.server.views;

import java.net.ServerSocket;
import java.net.Socket;

import com.t3.common.utils.Log;
import com.t3.server.controllers.Session;
import com.t3.server.models.AccountList;
import com.t3.server.models.BoardList;
import com.t3.server.models.SessionList;
import com.t3.server.models.UserList;

/**
 * @author Luan Vu
 *
 */
public class T3Server {
	private static final String TAG = T3Server.class.getName();
	
	/** Default port's server */
	public static final int DEFAULT_SERVER_PORT = 5000;
	
	/**The singleton server instance */
	private static T3Server instance = null;
	
	/** Server port */
	private int port;	
	
	/** Contain a list of user was logged after logged account .. */
	private UserList userList;
	
	/** Contain a list of board was created, playing/waiting */
	private BoardList boardList;
	
	/** Contain a list of account was logged in */
	private AccountList accountList;
	
	/** Contain a list of session meeting client */
	private SessionList sessionList;
	
	/** For calculator time from server begin start to startup done  */
	private long startTime;
	
	/** Default constructor */
	private T3Server() {
		System.out.println("------------------------------------------------------------------");
		System.out.println("          T I C - T A C - T O E   G A M E S   S E R V E R         ");
		System.out.println("------------------------------------------------------------------");
		System.out.println("------------------------ Hoàng Luân Vũ ---------------------------");
		System.out.println("------------------------ Phạm Văn Năm ----------------------------");
		System.out.println("------------------------------------------------------------------");
		System.out.println("----------------------- €yber$soft Team --------------------------");
		init();
		/**
		 * Note: we must create a server's configure file !!!!! (After)
		 */
		this.port = DEFAULT_SERVER_PORT;
	}
	
	public static T3Server getInstance() {
		if(instance == null)
			instance = new T3Server();
		return instance;
	}
	
	private void init() {
		startTime = System.currentTimeMillis();
		sessionList = new SessionList();
		Log.console(TAG, "Initialize session list OK");
		boardList = new BoardList();
		Log.console(TAG, "Initialize board list OK");
		userList = new UserList();
		Log.console(TAG, "Initialize user list OK");
		accountList = new AccountList();
		Log.console(TAG, "Initialize account list OK");
	}
	
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			Log.console(TAG, "Server listening on port " + port);
			Log.console(TAG, "Server startup in " + (System.currentTimeMillis() - startTime) + " ms");
			while (true) {
				Socket socket = server.accept();
				Log.console(TAG, "Client connected");
				Session connectionThread = new Session(socket);
				connectionThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public SessionList getSessionList() {
		return sessionList;
	}
	
	public UserList getUserList() {
		return userList;
	}

	public BoardList getBoardList() {
		return boardList;
	}
	
	public AccountList getAccountList() {
		return accountList;
	}

	public static void main(String[] args) {
		T3Server server = T3Server.getInstance();
		server.run();
	}
	
}
