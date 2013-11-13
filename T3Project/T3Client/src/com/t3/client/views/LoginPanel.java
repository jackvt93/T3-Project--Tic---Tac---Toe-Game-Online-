package com.t3.client.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.client.models.ClientListener;
import com.t3.client.models.LoginListener;
import com.t3.common.models.Bundle;
import com.t3.common.models.Package;
import com.t3.common.models.PackageKind;
import com.t3.common.models.User;
import com.t3.common.utils.ConfigUltils;
import com.t3.common.utils.Log;


public class LoginPanel extends JPanel implements ActionListener, ClientListener {

	private static final long serialVersionUID = 1L;
	private static final String TAG = LoginPanel.class.getName();

	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 350;

	private LoginListener loginListener = null;
	private SessionCommunicate sessionComm = null;

	private JTextField tfUsername;
	private JPasswordField jpPassword;
	private JButton btLogin,btCongfigure,btHelp,btQuit; 
	//private JCheckBox checkboxSavePassword;

	// User name and password to using it on receive
	private String userId;
	private char[] password;

	public LoginPanel(LoginListener loginListener){
		super(new BorderLayout());
		this.loginListener = loginListener;
		initialize();
	}

	private void initialize() {

		//title bar
		JPanel pinfo=new JPanel();
		pinfo.setBorder(BorderFactory.createLoweredBevelBorder());
		pinfo.setLayout(new BorderLayout());
		JLabel infoL=new JLabel("Cyber T3 v1.0");
		JLabel infoR=new JLabel("€yber$oft Team");
		pinfo.add(infoL,BorderLayout.WEST);
		pinfo.add(infoR,BorderLayout.EAST);
		this.add(pinfo,BorderLayout.SOUTH);

		//textfield

		Box bBorder=Box.createVerticalBox();
		JPanel pName=new JPanel();
		JPanel pPass=new JPanel();
		JPanel pLogin=new JPanel();
		JPanel pHelp=new JPanel();
		JPanel pConfigure=new JPanel();
		JPanel pQuit=new JPanel();


		JLabel name=new JLabel("Username: ");
		JLabel pass=new JLabel("Password: ");

		tfUsername=new JTextField(25);
		jpPassword=new JPasswordField(25);
		
		tfUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10){
					onLogin();
				}
			}
		});
		//title bar
		jpPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10){
					onLogin();
				}
			}
		});


		btLogin=new JButton("Login");
		btCongfigure=new JButton("Configure");
		btHelp=new JButton("Help");
		btQuit=new JButton("Quit");

		pName.add(name); pName.add(tfUsername);
		pPass.add(pass); pPass.add(jpPassword);

		btLogin.setPreferredSize(new Dimension(200, 25));
		btCongfigure.setPreferredSize(new Dimension(200, 25));
		btHelp.setPreferredSize(new Dimension(200, 25));
		btQuit.setPreferredSize(new Dimension(200, 25));


		pLogin.add(btLogin);
		pConfigure.add(btCongfigure);
		pHelp.add(btHelp);
		pQuit.add(btQuit);

		pass.setPreferredSize(name.getPreferredSize());

		pLogin.setPreferredSize(pConfigure.getPreferredSize());
		bBorder.add(Box.createVerticalStrut(25));
		bBorder.add(pName);
		bBorder.add(pPass);
		bBorder.add(Box.createVerticalStrut(10));
		bBorder.add(pLogin);
		bBorder.add(Box.createVerticalStrut(5));
		bBorder.add(pConfigure);
		bBorder.add(Box.createVerticalStrut(5));
		bBorder.add(pHelp);
		bBorder.add(Box.createVerticalStrut(5));
		bBorder.add(pQuit);
		bBorder.add(Box.createVerticalStrut(5));

		this.add(bBorder);


		btLogin.addActionListener(this);
		btCongfigure.addActionListener(this);
		btHelp.addActionListener(this);
		btQuit.addActionListener(this);
	}

	private void connect() throws Exception {
		Bundle bundle = ConfigUltils.getClientConfigure();
		String server = bundle.getString("server");
		int port = bundle.getInt("port");
		Socket socket = new Socket(server, port);
		sessionComm = new SessionCommunicate(socket, this);
		sessionComm.start();
	}

	private void onQuit() {
		int result = JOptionPane.showConfirmDialog(null, "Are you sure exit T3 ?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void onConfigure() {
		ConfigureDialog configureFrame = new ConfigureDialog();
		configureFrame.setVisible(true);
	}
	
	private void setEnableAllComponent(boolean enable) {
		btLogin.setEnabled(enable);
		btCongfigure.setEnabled(enable);
		btHelp.setEnabled(enable);
		btQuit.setEnabled(enable);
		tfUsername.setEditable(enable);
		jpPassword.setEditable(enable);
	}

	private void onLogin() {
		try {
			setEnableAllComponent(false);
			if (tfUsername.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "Please input username !", "Login", JOptionPane.WARNING_MESSAGE);
				setEnableAllComponent(true);
				tfUsername.requestFocus();
				return;
			} else if (jpPassword.getPassword().length == 0) {
				JOptionPane.showMessageDialog(null, "Please input password !", "Login", JOptionPane.WARNING_MESSAGE);
				setEnableAllComponent(true);
				jpPassword.requestFocus();
				return;
			}
			userId = tfUsername.getText();
			password = jpPassword.getPassword();
			if (sessionComm == null) {
				connect();
			}
			sessionComm.send(new Package(userId, password, PackageKind.ACTION_LOGIN));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Cannot connect to server !", "Connect error", JOptionPane.ERROR_MESSAGE);
			setEnableAllComponent(true);
			Log.logfile("Cannot connect to server", new Date(Calendar.getInstance().getTimeInMillis()));
			//e1.printStackTrace();
		}
	}
	
	private void createUser() throws IOException {
		while (true) {
			String name = JOptionPane.showInputDialog(null, "You first login to this account. Please input name to create user:\n Name can is a-z, 0-9 ", "Create new user", JOptionPane.QUESTION_MESSAGE);
			if (name != null && name.trim().length() > 0) {
				sessionComm.send(new Package(name, userId, PackageKind.ACTION_CREATE_USER));
				break;
			} 
			if (name != null && name.trim().length() == 0) {
				JOptionPane.showMessageDialog(null, "Name invalid", "Error", JOptionPane.WARNING_MESSAGE);
			}
			if (name == null) {
				// if user click cancel button, then send request log out, and log out account
				sessionComm.send(new Package(sessionComm.getUsername(), PackageKind.ACTION_LOGOUT));
				sessionComm.stopRun();
				sessionComm = null;
				setEnableAllComponent(true);
				break;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object.equals(btLogin)) {
			onLogin();
		} else if (object.equals(btCongfigure)) {
			onConfigure();
		} else if (object.equals(btQuit)) {
			onQuit();
		}
	}

	@Override
	public void onReceiveMessage(Object message) {
		try {
			Package pack = (Package)message;
			
			switch (pack.getKind()) {
			case PackageKind.RESULT_LOGIN_SUCCESS:
				Log.console(TAG, "Login success");
				User user = (User)pack.getData();
				/* User = null means is this account is the first login, not create user yet :D */
				if (user != null) {
					setEnableAllComponent(true);
					jpPassword.setText(""); 
					loginListener.onLoginSuccess(sessionComm, (User)pack.getData());
				} else {
					createUser();
				}
				break;
				
			case PackageKind.RESULT_LOGIN_USERID_FAIL:
				Log.console(TAG, "Username/password invalid");
				JOptionPane.showMessageDialog(null, "Username/password invalid", "Login info", JOptionPane.INFORMATION_MESSAGE);
				setEnableAllComponent(true);
				break;
				
			case PackageKind.RESULT_LOGIN_USERID_USED:
				Log.console(TAG, "Account is being used");
				JOptionPane.showMessageDialog(null, "Account is being used !", "Login info", JOptionPane.INFORMATION_MESSAGE);
				setEnableAllComponent(true);
				break;
				
			case PackageKind.RESULT_CREATE_USER_SUCCESS:
				Log.console(TAG, "Create user success");
				loginListener.onLoginSuccess(sessionComm, (User)pack.getData());
				break;
			case PackageKind.RESULT_CREATE_USER_EXISTED:
				Log.console(TAG, "User existed");
				JOptionPane.showMessageDialog(null, "This name is existed !", "Create user", JOptionPane.INFORMATION_MESSAGE);
				createUser();
				setEnableAllComponent(true);
				break;
			case PackageKind.RESULT_CREATE_USER_FAIL:
				Log.console(TAG, "Create user success");
				JOptionPane.showMessageDialog(null, "Has error while create user !!!", "Create user", JOptionPane.ERROR_MESSAGE);
				setEnableAllComponent(true);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
