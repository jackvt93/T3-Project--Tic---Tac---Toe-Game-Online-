
package com.t3.client.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * This class for show password board dialog
 * @author Luan Vu
 *
 */
public class BoardPasswordInputDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 110;
	
	private JPasswordField passwordPassword = new JPasswordField(20);
	private JButton buttonOK = new JButton("OK");
	private JButton buttonCancel = new JButton("Cancel");
	
	private char[] inputValue = null;
	
	public BoardPasswordInputDialog(){
		this(null, true);
		initialize();
	}
	
	public BoardPasswordInputDialog(Frame frame, boolean modal) {
		super(frame, modal);
		initialize();
	}

	private void initialize() {
		
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputValue = passwordPassword.getPassword();
				setVisible(false);
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		passwordPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					buttonOK.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		
		Box bBoreder=Box.createVerticalBox();
		
		JLabel jlPass=new JLabel("Password:"); 
		
		JPanel pName=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pPass=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pButton=new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		pPass.add(jlPass);
		pPass.add(passwordPassword);
		
		buttonOK.setPreferredSize(buttonCancel.getPreferredSize());
		
		pButton.add(buttonOK);
		pButton.add(buttonCancel);
		
		bBoreder.add(pName);
		bBoreder.add(pPass);
		
		this.add(bBoreder, BorderLayout.NORTH);
		this.add(pButton, BorderLayout.SOUTH);
		
		this.setTitle("Input board password");
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	
	public char[] getInputValue() {
		return this.inputValue;
	}
	
	/**
	 * The static method for show create room dialog
	 * @return A Bundle contains info of room (title and password)
	 * you can get info by get key "title" for get room's title
	 * key "password" for get room's password
	 * </br><b>Example:</b> </br>Bundle bundle = CreateRoomDialog.showDialog();
	 * </br>		String title = bundle.getString("title");
	 * </br>		 char[] password = bundle.getCharArray("password");
	 */
	public static char[] showDialog(Frame frame, boolean modal) {
		BoardPasswordInputDialog dialog = new BoardPasswordInputDialog(frame, modal);
		dialog.setVisible(true);
		return dialog.getInputValue();
	}
	
	/**
	 * The static method for show create room dialog with no argument
	 * @return A Bundle contains info of room (title and password)
	 * you can get info by get key "title" for get room's title
	 * key "password" for get room's password
	 * </br><b>Example:</b> </br>Bundle bundle = CreateRoomDialog.showDialog();
	 * </br>		 String title = bundle.getString("title");
	 * </br>		 char[] password = bundle.getCharArray("password");
	 */
	public static char[] showDialog() {
		return showDialog(null, true);
	}
	
}

