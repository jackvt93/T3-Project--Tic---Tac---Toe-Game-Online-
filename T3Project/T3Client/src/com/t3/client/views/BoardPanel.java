package com.t3.client.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.client.models.BoardListener;
import com.t3.client.models.Cell;
import com.t3.client.models.CellActionListener;
import com.t3.client.models.ClientListener;
import com.t3.common.models.Board;
import com.t3.common.models.Bundle;
import com.t3.common.models.Package;
import com.t3.common.models.PackageKind;
import com.t3.common.models.Player;
import com.t3.common.models.User;
import com.t3.common.utils.Log;

/**
 * This class extends JPanel using to Show Board game 
 * when join/create board success
 * @author Luan Vu
 *
 */
public class BoardPanel extends JPanel implements ActionListener, CellActionListener, ClientListener {
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = BoardPanel.class.getName();

	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGTH = 550;

	private static final int STATE_WAITING = 0x000000001;
	private static final int STATE_STARTED = 0x000000002;
	private static final int STATE_READY   = 0x000000003;

	/** That's size of board  */
	private static final int BOARD_DIMENSION = 3;

	private SessionCommunicate sessionComm = null;
	private BoardListener boardListener = null;
	private User user = null;
	private Board board = null;
	private boolean isOwner;
	private int currentState;
	private boolean myTurn = false;
	private char myToken = ' ';

	private JLabel labelPlayAt = new JLabel("Play at: ");
	private JButton buttonStartorReady = new JButton("Start");
	private JButton buttonResign = new JButton("Resign");
	private JButton buttonBacktoRoom = new JButton("Game room");
	private JButton buttonSendMessage = new JButton("Send");
	private JTextField textMessage = new JTextField(20);
	private DefaultListModel<String> modelPlayer = new DefaultListModel<>();
	private JList<String> jlistPlayer = new JList<String>(modelPlayer);
	private JTextArea textareaChatMessage = new JTextArea();
	private Cell[][] cell = new Cell[BOARD_DIMENSION][BOARD_DIMENSION]; 
	private String[] items = { "X", "O" };
	private JComboBox<String> comboSelectXO = null;

	public BoardPanel(SessionCommunicate sessionComm, Board board, User user, 
			BoardListener boardListener) {
		super(new BorderLayout());
		this.sessionComm = sessionComm;
		this.sessionComm.setClientListener(this);
		this.boardListener = boardListener;
		this.user = user;
		this.board = board;
		this.isOwner = (this.user.getName().equals(this.board.getOwnerName())) ? true : false; 
		setCurrentState(STATE_WAITING);
		initializeComponent();
		updateComponent();
	}

	private void initializeComponent() {
		buttonStartorReady.addActionListener(this);
		buttonResign.addActionListener(this);
		buttonBacktoRoom.addActionListener(this);
		buttonSendMessage.addActionListener(this);

		buttonResign.setEnabled(false);
		buttonStartorReady.setText(isOwner ? "Start" : "Ready");

		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		if (isOwner) {
			p1.add(labelPlayAt);
			comboSelectXO = new JComboBox<>(items);
			comboSelectXO.setEditable(false);
			p1.add(comboSelectXO);
		}

		p1.add(buttonStartorReady);
		p1.add(buttonResign);

		JPanel pp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pp.add(buttonBacktoRoom);
		p1.add(pp);


		JPanel p3 = new JPanel(new GridLayout(BOARD_DIMENSION, BOARD_DIMENSION));
		p3.setPreferredSize(new Dimension(300, 300));
		for (int i = 0; i < BOARD_DIMENSION; i++) {
			for (int j = 0; j < BOARD_DIMENSION; j++) {
				cell[i][j] = new Cell(i, j, this);
				cell[i][j].setEnabled(false);
				p3.add(cell[i][j]);
			}
		}

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p2.add(p3);

		Box b1 = Box.createVerticalBox();
		b1.add(p1);
		b1.add(p2);

		Box b2 = Box.createHorizontalBox();
		JScrollPane sp1 = new JScrollPane(jlistPlayer);
		sp1.setBorder(new TitledBorder("Players"));
		JScrollPane sp2 = new JScrollPane(textareaChatMessage);
		sp2.setBorder(new TitledBorder("Message"));
		b2.add(sp1);
		b2.add(sp2);

		Box b3 = Box.createHorizontalBox();
		b3.setBorder(new TitledBorder("Chat box"));
		b3.add(textMessage);
		b3.add(buttonSendMessage);

		this.add(b1, BorderLayout.NORTH);
		this.add(b2, BorderLayout.CENTER);
		this.add(b3, BorderLayout.SOUTH);

		/* 10-11-2013 Năm */
		textMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10)
					onSendMessage();
			}
		});

	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public int getBoardNumber() {
		return board.getBoardNumber();
	}

	@Override
	public void onReceiveMessage(Object message) {
		if (message == null) {
			return;
		}
		Package pack = (Package)message;

		switch (pack.getKind()) {

		case PackageKind.ACTION_BOARD_UPDATE_DATA: {
			this.board = (Board)pack.getData();
			updateComponent();
			break;
		}

		case PackageKind.RESULT_BOARD_LEAVE_OK:
			boardListener.onLeaveBoard(this.sessionComm, this.user);
			break;

		case PackageKind.ACTION_BOARD_LEAVE:
			JOptionPane.showMessageDialog(null, "The owner has leave board !", "Game", JOptionPane.INFORMATION_MESSAGE); 
			boardListener.onLeaveBoard(this.sessionComm, this.user);
			break;

		case PackageKind.RESULT_BOARD_START_OK:
			textareaChatMessage.append("Game: board start !\n");
			updateData((Board)pack.getData());
			updateComponent();
			break;

		case PackageKind.RESULT_BOARD_START_NOT_ENOUGH_PLAYER:
			textareaChatMessage.append("Game: not enough player to start !\n");
			break;

		case PackageKind.RESULT_BOARD_START_PLAYER_NOT_READY:
			textareaChatMessage.append("Game: some player not ready !\n");
			break;

		case PackageKind.RESULT_BOARD_START_FAIL:
			textareaChatMessage.append("Game: Has error while start !\n");
			break;

		case PackageKind.RESULT_BOARD_READY_OK:
			Log.console(TAG, "ready ok");
			setCurrentState(STATE_READY);
			this.board = (Board)pack.getData();
			updateComponent();
			break;

		case PackageKind.RESULT_BOARD_UNREADY_OK:
			setCurrentState(STATE_WAITING);
			this.board = (Board)pack.getData();
			updateComponent();
			break;

		case PackageKind.ACTION_BOARD_PLAYER_TICK: {
			Bundle bundle = (Bundle)pack.getData();
			int row = bundle.getInt("row");
			int column = bundle.getInt("column");
			tick(row, column, myToken == 'X' ? 'O' : 'X'); 
			myTurn = true;
			break;
		}

		case PackageKind.ACTION_BOARD_YOU_WON:
			JOptionPane.showMessageDialog(null, "Congratulation, You won ^.^", "Game", JOptionPane.PLAIN_MESSAGE); 
			setCurrentState(STATE_WAITING);
			updateComponent();
			break;

		case PackageKind.ACTION_BOARD_YOU_LOSE: {
			Bundle bundle = (Bundle)pack.getData();
			int row = bundle.getInt("row");
			int column = bundle.getInt("column");
			tick(row, column, myToken == 'X' ? 'O' : 'X'); 
			JOptionPane.showMessageDialog(null, "You lose !", "Game", JOptionPane.PLAIN_MESSAGE); 
			setCurrentState(STATE_WAITING);
			updateComponent();
			break;
		}

		case PackageKind.ACTION_BOARD_YOU_DRAW: {
			JOptionPane.showMessageDialog(null, "Draw !", "Game", JOptionPane.PLAIN_MESSAGE); 
			setCurrentState(STATE_WAITING);
			updateComponent();
		}

		case PackageKind.RESULT_MESSAGE_BOARD:
			String s = (String)pack.getData();
			textareaChatMessage.append(""+s+"\n");
			break;
		}
	}

	private void tick(int row, int column, char token) {
		cell[row][column].setToken(token);
	}

	private void updateComponent() {
		switch (getCurrentState()) {

		case STATE_READY: {
			buttonStartorReady.setText("Not ready");
			buttonBacktoRoom.setEnabled(false);
			modelPlayer.removeAllElements();
			for (Player player : this.board.getPlayerList().getPlayers()) {
				String display = player.getName();
				if(display.equals(board.getOwnerName())) {
					display += " (Host)";
				}
				else {
					display += player.isReady() ? " (Ready)" : " (Not ready)";
				}
				modelPlayer.addElement(display);
			}
			break;
		}

		case STATE_STARTED: {	
			buttonResign.setEnabled(true);
			buttonStartorReady.setEnabled(false);
			buttonBacktoRoom.setEnabled(false);

			for (int i = 0; i < BOARD_DIMENSION; i++) {
				for (int j = 0; j < BOARD_DIMENSION; j++) {
					this.cell[i][j].clean();
					this.cell[i][j].setEnabled(true);
				}
			}

			modelPlayer.removeAllElements();
			for (Player player : this.board.getPlayerList().getPlayers()) {
				String display = player.getName();
				display += " (" + player.getToken() +")";
				modelPlayer.addElement(display);
			}

			if (isOwner) {
				comboSelectXO.setEnabled(false);
			}
			break;
		}

		case STATE_WAITING: {
			buttonResign.setEnabled(false);
			buttonStartorReady.setEnabled(true);

			if(!isOwner) 
				buttonStartorReady.setText("Ready");
			else
				comboSelectXO.setEnabled(true);

			buttonBacktoRoom.setEnabled(true);
			setEnableBoard(false);

			modelPlayer.removeAllElements();
			for (Player player : this.board.getPlayerList().getPlayers()) {
				String display = player.getName();
				if(display.equals(board.getOwnerName())) {
					display += " (Host)";
				}
				else {
					display += player.isReady() ? " (Ready)" : " (Not ready)";
				}
				modelPlayer.addElement(display);
			}
			break;
		}
		}
	}

	private void updateData(Board board) {
		this.board = board;
		if(isOwner) {
			myTurn = true;
			myToken = board.getOwner().getToken();
		} else {
			myTurn = false;
			myToken = board.getNotOwner().getToken();
		}
		setCurrentState(STATE_STARTED);
	}

	private void setEnableBoard(boolean enabled) {
		for (int i = 0; i < BOARD_DIMENSION; i++) {
			for (int j = 0; j < BOARD_DIMENSION; j++) {
				cell[i][j].setEnabled(enabled);
			}
		}
	}

	public String EditString(String s){
		String str="";
		str = DateFormat.getTimeInstance().format(new Date())+ " -- "+
				this.user.getName()+" : "+s;
		return str;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();

		if (object.equals(buttonStartorReady)) {
			onStartOrReady();
		} else if (object.equals(buttonResign)) {
			onResign();
		} else if (object.equals(buttonBacktoRoom)) {
			onBackToRoom();
		} else if(object.equals(buttonSendMessage)) {
			onSendMessage();
		} 

	}


	/* 10-11-2013 Năm */
	private void onSendMessage() {
		try {
			String s = this.user.getName() + ": " + textMessage.getText().toString();
			textareaChatMessage.append(s + "\n");
			Package package1 = new Package(s, board.getBoardNumber(), PackageKind.ACTION_SEND_BOARD_MESSAGE);
			sessionComm.send(package1);
			textMessage.setText("");
		} catch (Exception e) {
			Log.logfile(e.getMessage(), new Date(Calendar.getInstance().getTimeInMillis()));
		}
	}

	private void onBackToRoom() {
		try {
			if(getCurrentState() == STATE_READY || getCurrentState() == STATE_STARTED) {
				return;
			}
			sessionComm.send(new Package(this.board.getBoardNumber(), this.user.getName(),PackageKind.ACTION_BOARD_LEAVE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onResign() {
		Log.console(TAG, "send request resign");

	}

	private void onStartOrReady() {
		try {
			if(isOwner) {
				Log.console(TAG, "send request start");
				char token = ((String)comboSelectXO.getSelectedItem()).charAt(0);
				sessionComm.send(new Package(board.getBoardNumber(), token, PackageKind.ACTION_BOARD_START));
			} else {
				if(getCurrentState() == STATE_WAITING) {
					Log.console(TAG, "send request ready");
					sessionComm.send(new Package(board.getBoardNumber(), user.getName(),PackageKind.ACTION_BOARD_READY));
				} else if(getCurrentState() == STATE_READY) {
					Log.console(TAG, "send request not ready");
					sessionComm.send(new Package(board.getBoardNumber(), user.getName(),PackageKind.ACTION_BOARD_UNREADY));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCellClick(int row, int column) {
		try {
			Cell cell = this.cell[row][column];
			if (cell.getToken() == ' ' && myTurn) {
				cell.setToken(myToken);
				myTurn = false;
				Bundle bundle = new Bundle();
				bundle.putInt("row", row);
				bundle.putInt("column", column);
				sessionComm.send(new Package(this.board.getBoardNumber(), bundle, PackageKind.ACTION_BOARD_PLAYER_TICK));
			}
		} catch (Exception e) {
			Log.logfile(e.getMessage(), new Date(Calendar.getInstance().getTimeInMillis()));
			e.printStackTrace();
		}
	}



}
