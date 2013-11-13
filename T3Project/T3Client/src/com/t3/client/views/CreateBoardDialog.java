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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.t3.common.models.Bundle;


/**
 * This class for show create board dialog
 * @author Luan Vu
 *
 */
public class CreateBoardDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 147;
	
	private JTextField textTitle = new JTextField("Let's play tic-tac-toe together !!!", 20);
	private JPasswordField passwordPassword = new JPasswordField(20);
	private JButton buttonOK = new JButton("OK");
	private JButton buttonCancel = new JButton("Cancel");
	
	private Bundle inputValue = null;
	
	public CreateBoardDialog(){
		this(null, true);
		initialize();
	}
	
	public CreateBoardDialog(Frame frame, boolean modal) {
		super(frame, modal);
		initialize();
	}

	private void initialize() {
		
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputValue = new Bundle();
				inputValue.putString("title", textTitle.getText());
				inputValue.putCharArray("password", passwordPassword.getPassword());
				setVisible(false);
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		textTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					buttonOK.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		
		passwordPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					buttonOK.doClick();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					buttonCancel.doClick();
				}
			}
		});
		
		Box bBoreder=Box.createVerticalBox();
		bBoreder.setBorder(new TitledBorder("Board info"));
		
		JLabel jlName=new JLabel("Title:"); 
		JLabel jlPass=new JLabel("Password:"); 
		
		jlName.setPreferredSize(jlPass.getPreferredSize());
		
		JPanel pName=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pPass=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pButton=new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		pName.add(jlName);
		pName.add(textTitle);
		
		pPass.add(jlPass);
		pPass.add(passwordPassword);
		
		buttonOK.setPreferredSize(buttonCancel.getPreferredSize());
		
		pButton.add(buttonOK);
		pButton.add(buttonCancel);
		
		bBoreder.add(pName);
		bBoreder.add(pPass);
		
		this.add(bBoreder, BorderLayout.NORTH);
		this.add(pButton, BorderLayout.SOUTH);
		
		this.setTitle("Create a new board");
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	
	public Bundle getInputValue() {
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
	public static Bundle showDialog(Frame frame, boolean modal) {
		CreateBoardDialog dialog = new CreateBoardDialog(frame, modal);
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
	public static Bundle showDialog() {
		return showDialog(null, true);
	}
	
}
