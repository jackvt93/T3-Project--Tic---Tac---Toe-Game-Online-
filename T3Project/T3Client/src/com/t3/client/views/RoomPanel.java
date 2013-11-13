package com.t3.client.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.client.models.ClientListener;
import com.t3.client.models.RoomListener;
import com.t3.common.models.Board;
import com.t3.common.models.Bundle;
import com.t3.common.models.Package;
import com.t3.common.models.PackageKind;
import com.t3.common.models.User;
import com.t3.common.utils.Log;

/**
 * @author Luan Vu
 *
 */
public class RoomPanel extends JPanel implements ActionListener, ClientListener {

	private static final long serialVersionUID = 1L;
	private static final String TAG = RoomPanel.class.getName();

	public static final int DEFAULT_WIDTH = 700;
	public static final int DEFAULT_HEIGHT = 600;


	private SessionCommunicate sessionComm = null;

	private RoomListener roomListener = null;

	private User user = null;

	private JButton buttonCreate = new JButton("Create");
	private JButton buttonJoin = new JButton("Join");
	private JButton buttonSend = new JButton("Send");

	private JTextField textSendMessage = new JTextField();

	private JTextArea textAreaRoomMessage = new JTextArea();

	String[] headerTable =  { "Num", "Title" };
	String[] headerUser =  { "Name", "Rating" };
	private DefaultTableModel modelBoard;
	private DefaultTableModel modelUser;

	private JTable jtableUsers;
	private JTable jtableBoards;


	private Vector<User> userList;
	private Vector<Board> boardList;


	public RoomPanel(SessionCommunicate sessionComm, User user, RoomListener roomListener) {
		super(new BorderLayout());
		this.sessionComm = sessionComm;
		this.sessionComm.setClientListener(this);
		this.roomListener = roomListener;
		this.user = user;
		initializeComponent();
		initializeData();
	}

	private void initializeComponent() {
		buttonCreate.addActionListener(this);
		buttonJoin.addActionListener(this);
		buttonSend.addActionListener(this);

		textAreaRoomMessage.setEditable(false);
		textSendMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10){
					onSend();
				}
			}
		});
		modelBoard = new DefaultTableModel(headerTable, 0);
		jtableBoards = new JTable(modelBoard) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jtableUsers = new JTable(modelUser = new DefaultTableModel(headerUser, 0)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JPanel panelGameroom = new JPanel(new BorderLayout(7, 7));

		Box b1 = Box.createHorizontalBox();
		b1.add(buttonCreate);
		b1.add(Box.createHorizontalStrut(5));
		b1.add(buttonJoin);

		JSplitPane splitPaneGameRoom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(jtableBoards), 
				new JScrollPane(textAreaRoomMessage));
		splitPaneGameRoom.setBorder(new TitledBorder("Board list"));
		splitPaneGameRoom.setDividerLocation(DEFAULT_HEIGHT/2);

		Box b2 = Box.createHorizontalBox();
		b2.add(textSendMessage);
		b2.add(Box.createHorizontalStrut(5));
		b2.add(buttonSend);

		panelGameroom.add(b1, BorderLayout.NORTH);
		panelGameroom.add(splitPaneGameRoom);
		panelGameroom.add(b2, BorderLayout.SOUTH);

		JPanel panelListUser = new JPanel(new BorderLayout(10, 10));
		panelListUser.add(new JScrollPane(jtableUsers));
		panelListUser.setBorder(new TitledBorder("Users"));
		panelListUser.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		JSplitPane splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelGameroom, panelListUser);
		splitPaneMain.setDividerLocation(DEFAULT_WIDTH * 2 / 3);
		this.add(splitPaneMain);

		textAreaRoomMessage.requestFocus();
	}

	private void initializeData() {
		try {
			// Send request get user list to server
			sessionComm.send(new Package(null, PackageKind.ACTION_GET_USER_LIST));

			// Send request get table list to server
			sessionComm.send(new Package(null, PackageKind.ACTION_GET_BOARD_LIST));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if(object.equals(buttonCreate)) {
			onCreate();
		} else if (object.equals(buttonJoin)) {
			onJoin();
		} else if (object.equals(buttonSend)) {
			onSend();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onReceiveMessage(Object message) {

		if (message == null) 
			return;

		Package pack = (Package)message;

		switch (pack.getKind()) {

		case PackageKind.RESULT_GET_USER_LIST:
			Log.console(TAG, "receive user list");
			userList = (Vector<User>)pack.getData();
			updateDisplayUserList();
			break;

		case PackageKind.RESULT_GET_BOARD_LIST:
			Log.console(TAG, "receive board list");
			boardList = (Vector<Board>)pack.getData();
			updateDisplayBoardList();
			break;

		case PackageKind.RESULT_CREATE_BOARD_SUCCESS: {
			Log.console(TAG, "create board success");
			Board board = (Board)pack.getData();

			if(board == null)
				return;

			this.roomListener.onCreateBoardSuccess(sessionComm, user, board);
			break;
		}

		/* 2013 - 8 - 11 : Nam */
		case PackageKind.RESULT_MESSAGE_ROOM:
			String s=(String)pack.getData();
			textAreaRoomMessage.append(s+"\n");
			break;

			/* 2013 - 9 - 11 : Nam (10:20PM) */
		case PackageKind.ACTION_INPUT_PASS_BOARD: {
			try {
				int boardNum = (int)pack.getData();
				char[] password = BoardPasswordInputDialog.showDialog();
				Bundle bundle = new Bundle();
				bundle.putInt("boardNumber", boardNum);
				bundle.putCharArray("password", password);

				if (password != null) {
					sessionComm.send(new Package(this.user, bundle, PackageKind.RESULT_PASSWORD_BOARD));
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.logfile(e.getMessage(), new Date(Calendar.getInstance().getTimeInMillis()));
			}
			break;
		}

		/* 2013 - 8 - 11 : Nam */
		case PackageKind.RESULT_JOIN_BOARD_SUCCESS: {
			Log.console(TAG, "join board success");
			Board board = (Board)pack.getData();

			if (board == null)
				return;

			this.roomListener.onJoinBoardSuccess(sessionComm, user, board);
			break;
		}
		
		case PackageKind.RESULT_JOIN_BOARD_PASSW_WRONG:
			JOptionPane.showMessageDialog(null, "Password not correct !", "Game", JOptionPane.INFORMATION_MESSAGE);
			break;

		/* 2013 - 8 - 11 : Nam */
		case PackageKind.RESULT_JOIN_BOARD_FULL:
			JOptionPane.showMessageDialog(null, "This board is full !!", "Notification", JOptionPane.INFORMATION_MESSAGE);
			break;

			/* 2013 - 8 - 11 : Nam */
		case PackageKind.RESULT_JOIN_BOARD_FAIL:
			JOptionPane.showMessageDialog(null, "Error when join board", "Notification", JOptionPane.ERROR_MESSAGE);
			break;

		case PackageKind.RESULT_JOIN_BOARD_NOT_EXIST:
			JOptionPane.showMessageDialog(null, "This board not exist", "Notification", JOptionPane.INFORMATION_MESSAGE);
			break;

		case PackageKind.ACTION_ROOM_UPDATE_USER_LIST: {
			Log.console(TAG, "Update user");
			boolean isUpdate = (boolean)pack.getExtras();
			if(pack.getData() == null) 
				break;
			updateUserList(pack.getData(), isUpdate); 
			break;
		}

		case PackageKind.ACTION_ROOM_UPDATE_BOARD_LIST: {
			Log.console(TAG, "Update board");
			boolean isUpdate = (boolean)pack.getExtras();
			if(pack.getData() == null) 
				break;
			updateBoardList(pack.getData(), isUpdate); 
			break;
		}
		
		case PackageKind.RESULT_LOGOUT_OK:
			Log.console(TAG, "Logot");
			roomListener.onLogoutOK();
			break;
		
		}
	}

	/**
	 * @param user need to update to list
	 * @param flag true if update is add into list, false if delete from list
	 */
	private void updateUserList(Object object, boolean flag) {
		if(flag) {
			User user = (User)object;
			String [] row = {user.getName(), displayRating(user.getRating())};
			modelUser.addRow(row);
		} else {
			String username = (String)object;
			for (int i = 0; i < modelUser.getRowCount(); i++) {
				String name = (String)modelUser.getValueAt(i, 0);
				if(name.equals(username)) {
					modelUser.removeRow(i);
					return;
				}
			}

		}
	}

	/**
	 * @param user need to update to list
	 * @param flag true if update is add into list, false if delete from list
	 */
	private void updateBoardList(Object object, boolean flag) {
		if(flag) {
			Board board = (Board)object;
			String [] row = {board.getBoardNumber() + "", board.getTitle() + ""};
			modelBoard.addRow(row);
		} else {
			int boardNumber = (int)object;
			for (int i = 0; i < modelBoard.getRowCount(); i++) {
				try {
					int number = Integer.parseInt(modelBoard.getValueAt(i, 0).toString());
					if(number == boardNumber) {
						modelBoard.removeRow(i);
						return;
					}
				} catch (Exception e) {
					Log.logfile(e.getMessage(), new Date(Calendar.getInstance().getTimeInMillis()));
				}
			}

		}
	}

	private void updateDisplayUserList() {
		for (User user : userList) {
			String [] row = {user.getName(), displayRating(user.getRating())};
			modelUser.addRow(row);
		}
	}

	private void updateDisplayBoardList() {
		for (Board board : boardList) {
			String [] row = {board.getBoardNumber() + "", board.getTitle() + ""};
			modelBoard.addRow(row);
		}
	}

	private String displayRating(int rating) {
		String sRet = "";
		for (int i = 1; i <= rating; i++) {
			sRet += "♫";
		}
		return sRet;
	}

	private void onCreate() {
		try {
			Bundle bundle = CreateBoardDialog.showDialog();
			if (bundle != null) {
				Package pack = new Package(user, bundle, PackageKind.ACTION_CREATE_BOARD);
				sessionComm.send(pack);
			}
		} catch (IOException e) {
			Log.console(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	private void onJoin() {
		try {
			if (jtableBoards.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(null, "Please select room to join", "Join Room", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int boardNumber = Integer.parseInt(modelBoard.getValueAt(jtableBoards.getSelectedRow(), 0).toString());
			Package pack = new Package(boardNumber, user, PackageKind.ACTION_JOIN_BOARD);
			sessionComm.send(pack);
		} catch (Exception e) {
			Log.console(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	/** 2013 - 8 - 11 : Nam*/
	private void onSend() {
		try {
			Bundle bundle = new Bundle();
			String s = EditString(textSendMessage.getText().toString());
			textAreaRoomMessage.append(s + "\n");
			bundle.putString("message", s);
			Package pack = new Package(user, bundle,
					PackageKind.ACTION_SEND_ROOM_MESSAGE);
			sessionComm.send(pack);
			textSendMessage.setText("");
		} catch (Exception e) {
			Log.console(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	/** 2013 - 8 - 11 : Nam*/
	//edit String before append in Text Arena
	public String EditString(String s){
		String str="";
		str = DateFormat.getTimeInstance().format(new Date())+ " -- "+
				this.user.getName()+" : "+s;
		return str;
	}

}
