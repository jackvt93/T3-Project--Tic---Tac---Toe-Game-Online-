package com.t3.client.models;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 * This class extends JButton contains info of a cell in board
 * @author Luan Vu
 *
 */
public class Cell extends JButton implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int row;
	private int column;
	private char token = ' ';
	private CellActionListener actionListener;
	
	public Cell(int row, int column, CellActionListener actionListener) {
		super.setBackground(Color.WHITE);
		this.row = row;
		this.column = column;
		this.actionListener = actionListener;
		addActionListener(this);
	}
	
	public char getToken() {
		return token;
	}

	public void setToken(char token) {
		this.token = token;
		
		if(token == 'X') {
			ImageIcon icon = new ImageIcon("icons/Xicon.png");
			this.setIcon(icon);
		} else if(token == 'O'){
			ImageIcon icon = new ImageIcon("icons/Oicon.png");
			this.setIcon(icon);
		} else {
			this.setIcon(null);
		}
	}
	
	public void clean() {
		this.setToken(' '); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.actionListener.onCellClick(row, column);
	}
	
	
	
}
