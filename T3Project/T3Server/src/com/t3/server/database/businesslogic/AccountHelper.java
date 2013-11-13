package com.t3.server.database.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.t3.server.database.entities.Account;

public class AccountHelper {
	private static final String TABLE_NAME = "Account";
	
	private static final String AID = "AID";
	private static final String NAME = "Name";
	private static final String EMAIL = "Email";
	private static final String SEX = "Sex";
	private static final String ADDRESS = "Address";
	
	private Connection conn;
	
	public AccountHelper() throws Exception {
		conn = ConnectionFactory.getInstance();
	}
	
	public Account getAccount(int id) throws SQLException {
		String sql = "SELECT TOP(1) * FROM [" + TABLE_NAME + "] WHERE " + AID + " = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, id);
		ResultSet resultSet = statement.executeQuery();
		return new Account(resultSet.getInt(AID), 
				resultSet.getString(NAME), 
				resultSet.getString(EMAIL), 
				resultSet.getString(SEX), 
				resultSet.getString(ADDRESS));
	}
}
