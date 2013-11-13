package com.t3.server.models;

import java.util.HashMap;
import java.util.Vector;

import com.t3.common.models.Board;
import com.t3.common.models.User;


/**
 * Class contain boards created in server, waiting for playing/playing 
 * @author Luan Vu
 *
 */
public class BoardList {
	
	private HashMap<Integer, Board> boardList;
	
	/**
	 * A list of unused board numbers for general create new board number ^^
	 */
	private Vector<Integer> unusedBoardNumber;
	
	/**
	 * A number for count board number
	 */
	private int countBoardNum;
	
	
	/**
	 * Default constructor
	 */
	public BoardList() {
		this.boardList = new HashMap<Integer, Board>();
		this.unusedBoardNumber = new Vector<Integer>();
		this.countBoardNum = 0;
	}

	public Board createBoard(User creator, String title, char[] password) {
		Integer newBoardNumber = generalBoardNumber();
		Board newBoard = new Board(newBoardNumber, title, password, creator);
		this.boardList.put(newBoardNumber, newBoard);
		return newBoard;
	}
	
	public void removeBoard(Integer boardNumber) {
		boardList.remove(boardNumber);
		
		if (boardNumber == countBoardNum) {
			countBoardNum--;
		} else {
			unusedBoardNumber.add(boardNumber);
		}
	}
	
	public Board getBoard(Integer boardNumber) {
		return boardList.get(boardNumber);
	}
	
	public Vector<Board> getAllBoard() {
		return new Vector<Board>(boardList.values());
	}
	
	public int size() {
		return boardList.size();
	}
	
	private Integer generalBoardNumber() {
		if(unusedBoardNumber.size() == 0) {
			return new Integer(++countBoardNum);
		}
		return unusedBoardNumber.remove(0);
	}
}
