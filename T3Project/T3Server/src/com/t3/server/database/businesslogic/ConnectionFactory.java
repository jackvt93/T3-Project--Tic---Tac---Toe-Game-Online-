package com.t3.server.database.businesslogic;

import java.sql.Connection;
import java.sql.DriverManager;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class ConnectionFactory {
	private static Connection instance = null;
	
	public static Connection getInstance() throws Exception {
		if (instance == null) {
			Class.forName(SQLServerDriver.class.getName());
			instance = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=T3DB", "sa", "123456");
		}
		return instance;
	}
}
