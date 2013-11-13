package com.t3.server.database.businesslogic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.t3.server.database.entities.Login;

public class LoginHelper {
	private static final String TABLE_NAME = "Login";
	
	private static final String USERID = "UserId";
	private static final String PASSWORD = "Password";
	private static final String LASTCONNDATE = "LastConnDate";
	private static final String AID = "AID";

	private Connection conn;

	public LoginHelper() throws Exception {
		conn = ConnectionFactory.getInstance();
	}

	/**
	 * this method using to login account 
	 * @param username
	 * @param password
	 * @return account id -1 if login fail, > 0 if success
	 * @throws SQLException
	 */
	public int login(String username, char[] password) throws SQLException {
		String sql = "SELECT * FROM [" + TABLE_NAME + "] WHERE " + USERID
				+ " LIKE ? AND " + PASSWORD + " LIKE ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, username);
		statement.setString(2, new String(password));
		ResultSet result = statement.executeQuery();
		if (result.next()) {
			return result.getInt(AID);
		}
		return -1;
	}
	
	public boolean update(Login login) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET " + USERID + " = ?" + 
														PASSWORD + " = ?" + 
														LASTCONNDATE + " = ?" + 
														AID + " = ? WHERE " + USERID + " = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, login.getUsername());
		statement.setString(2, login.getPassword());
		statement.setDate(3, login.getLastConnDate());
		statement.setInt(4, login.getAid());
		return statement.executeUpdate() > 0;
	}
	
	public boolean updateConnDate(String userId) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET " + LASTCONNDATE + " = ? WHERE " + USERID + " = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
		statement.setString(2, userId);
		return statement.executeUpdate() > 0;
	}
	
	public boolean insert(Login login) {
		return false;
	}
}
