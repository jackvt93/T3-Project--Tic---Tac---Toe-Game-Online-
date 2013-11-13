package com.t3.client.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.client.models.BoardListener;
import com.t3.client.models.LoginListener;
import com.t3.client.models.RoomListener;
import com.t3.common.models.Board;
import com.t3.common.models.Package;
import com.t3.common.models.PackageKind;
import com.t3.common.models.User;
import com.t3.common.utils.Log;

/**
 * 
 * @author Luan Vu
 *
 */
public class T3Client extends JFrame implements LoginListener, RoomListener, BoardListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = T3Client.class.getName();

	private static final int STATE_LOGIN 	= 0x00000001;
	private static final int STATE_ROOM 	= 0x00000002;
	private static final int STATE_BOARD 	= 0x00000003;
	
	private SessionCommunicate sessionComm = null;

	private JMenuBar menubar = new JMenuBar();
	private JMenu menuFile = new JMenu("File");
	private JMenu menuHelp = new JMenu("Help");
	private JMenuItem menuFileLogout = new JMenuItem("Logout");
	private JMenuItem menuFileExit = new JMenuItem("Exit");
	private JMenuItem menuHelpAbout = new JMenuItem("About");

	private int currentState = STATE_LOGIN;

	private LoginPanel loginPanel = null;
	private RoomPanel roomPanel = null;
	private BoardPanel boardPanel = null;

	public T3Client() {
		loginPanel = new LoginPanel(this);
		initialize();
	}

	private void initialize() {
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowActivated(WindowEvent e) {
				super.windowActivated(e);
				Log.logfile("", new Date(Calendar.getInstance().getTimeInMillis()));
			}

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
//					int nRet = JOptionPane.showConfirmDialog(null, "Are you sure to exit ?", "Exit confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//					if (nRet == JOptionPane.YES_OPTION) {
						if (getCurrentState() == STATE_BOARD) {
							sessionComm.send(new Package(boardPanel.getBoardNumber(), sessionComm.getUsername(), PackageKind.ACTION_BOARD_LEAVE));
						}
						if (sessionComm != null && sessionComm.isRunning()) {
							sessionComm.send(new Package(null, PackageKind.ACTION_LOGOUT));
							sessionComm.stopRun();
						}
//					} else {
//					}

				} catch (Exception e2) {
					Log.logfile(e2.getMessage(), new Date(Calendar.getInstance().getTimeInMillis()));
					e2.printStackTrace();
				}
			}
		});
		menuFile.add(menuFileLogout);
		menuFile.add(menuFileExit);
		
		menuHelp.add(menuHelpAbout);
		
		menubar.add(menuFile);
		menubar.add(menuHelp);
		
		menuFileLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int nAns = JOptionPane.showConfirmDialog(null, "Are you sure logout ?", "Logout confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (nAns == JOptionPane.OK_OPTION) {
						if (sessionComm != null && sessionComm.isRunning()) {
							sessionComm.send(new Package(null, PackageKind.ACTION_LOGOUT));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.logfile(e.getMessage(), new Date(Calendar.getInstance().getTimeInMillis()));  
				}
			}
		});
		
		this.setJMenuBar(menubar);
		updateUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	private void updateBaseUI(JPanel content, int width, int height, boolean resizeable) {
		this.setContentPane(content);
		this.setResizable(resizeable);
		this.setSize(width, height);
	}
	
	private void updateUI() {
		switch (getCurrentState()) {
		case STATE_LOGIN:
			Log.console(TAG, "STATE LOGIN");
			menubar.setVisible(false);
			updateBaseUI(loginPanel, LoginPanel.DEFAULT_WIDTH, LoginPanel.DEFAULT_HEIGHT, false);
			break;
			
		case STATE_ROOM:
			Log.console(TAG, "STATE ROOM");
			menubar.setVisible(true);
			this.setJMenuBar(menubar);
			updateBaseUI(roomPanel, RoomPanel.DEFAULT_WIDTH, RoomPanel.DEFAULT_HEIGHT, true);
			this.setLocationRelativeTo(null);
			break;
			
		case STATE_BOARD:
			Log.console(TAG, "STATE BOARD");
			menubar.setVisible(false);
			updateBaseUI(boardPanel, this.getWidth() + 1, this.getHeight(), true);
			break;
		}
	}
	
	@Override
	public void onLoginSuccess(SessionCommunicate sessionComm, User user) {
		Log.console(TAG, "Login success");
		this.sessionComm = sessionComm;
		this.sessionComm.setUsername(user.getName());
		roomPanel = new RoomPanel(sessionComm, user, this);
		this.setTitle("User: " + user.getName());
		setCurrentState(STATE_ROOM);
		updateUI();
	}

	@Override
	public void onCreateBoardSuccess(SessionCommunicate sessionComm, User user, Board board) {
		Log.console(TAG, "Create table success");
		Log.console(TAG, user.getName());
		boardPanel = new BoardPanel(sessionComm, board, user, this);
		this.setTitle("User: " + this.sessionComm.getUsername() + " - board " + board.getBoardNumber() + " : " + board.getTitle());
		setCurrentState(STATE_BOARD);
		updateUI();
	}

	/** 2013 - 9 - 11  : Nam */
	@Override
	public void onJoinBoardSuccess(SessionCommunicate sessionComm, User user, Board board) {
		Log.console(TAG, "Join board " + board.getBoardNumber());
		boardPanel = new BoardPanel(sessionComm, board, user, this);
		this.setTitle("User: " + this.sessionComm.getUsername() + " - board " + board.getBoardNumber() + " : " + board.getTitle());
		setCurrentState(STATE_BOARD);
		updateUI();
	}
	
	@Override
	public void onLogoutOK() {
		Log.console(TAG, "Log out");
		sessionComm.stopRun();
		setCurrentState(STATE_LOGIN);
		updateUI();
	}



	@Override
	public void onLeaveBoard(SessionCommunicate sessionComm, User user) {
		Log.console(TAG, "Leave board ");
		roomPanel = new RoomPanel(sessionComm, user, this);
		this.setTitle("User: " + user.getName());
		setCurrentState(STATE_ROOM);
		updateUI();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new T3Client().setVisible(true);
			}
		});
	}
}
