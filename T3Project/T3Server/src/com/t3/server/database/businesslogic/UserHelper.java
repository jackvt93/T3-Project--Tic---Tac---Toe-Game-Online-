package com.t3.server.database.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.t3.server.database.entities.User;

public class UserHelper {
	public final static int CREATE_USER_SUCCESS 	= 0x0001;
	public final static int CREATE_USER_EXISTED 	= 0x0002;
	public final static int CREATE_USER_FAIL		= 0x0003;

	private final static String TABLE_NAME = "User";

	private final static String NAME = "Name";
	private final static String RATING = "Rating";
	private final static String WINS = "Wins";
	private final static String LOSES = "Loses";
	private final static String DRAWS = "Draws";
	private final static String AID = "AID";

	private Connection conn;

	public UserHelper() throws Exception {
		conn = ConnectionFactory.getInstance();
	}

	/**
	 * @param accountId : need for get user
	 * @return database user object
	 * @throws SQLException
	 */
	public User getUser(int accountId) throws SQLException {
		String sql = "SELECT TOP(1) * FROM [" + TABLE_NAME + "] WHERE " + AID + " = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, accountId);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			return new User(resultSet.getString(NAME),
					resultSet.getInt(RATING), 
					resultSet.getInt(WINS),
					resultSet.getInt(LOSES), 
					resultSet.getInt(DRAWS),
					resultSet.getInt(AID));
		}
		return null;
	}
	
	public User getUser(String username) throws SQLException {
		String sql = "SELECT TOP(1) * FROM [" + TABLE_NAME + "] WHERE " + NAME + " LIKE ?"; 
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, username);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			return new User(resultSet.getString(NAME),
					resultSet.getInt(RATING), 
					resultSet.getInt(WINS),
					resultSet.getInt(LOSES), 
					resultSet.getInt(DRAWS),
					resultSet.getInt(AID));
		}
		return null;
	}

	/**
	 * This method for get common user for send from server to client
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public com.t3.common.models.User getCommonUser(int accountId) throws SQLException {
		String sql = "SELECT TOP(1) * FROM [" + TABLE_NAME + "] WHERE " + AID + " = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, accountId);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			return new com.t3.common.models.User(resultSet.getString(NAME),
					resultSet.getInt(RATING), 
					resultSet.getInt(WINS),
					resultSet.getInt(LOSES), 
					resultSet.getInt(DRAWS));
		}
		return null;
	}

	/**
	 * @param user object need to create new
	 * @return true if create success, false if create fail
	 * @throws SQLException
	 */
	public int create(User user) throws SQLException {

		if (getUser(user.getName()) != null) {
			return CREATE_USER_EXISTED;
		}
		String sql = "INSERT INTO [" + TABLE_NAME + "]" + " ([" + NAME + "], [" + AID + "])" + 
		" VALUES (?, ?)";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, user.getName());
		statement.setInt(2, user.getAid());
		return (statement.executeUpdate() > 0) ? CREATE_USER_SUCCESS : CREATE_USER_FAIL;
	}

	public boolean update(com.t3.common.models.User user) throws SQLException {
		String sql = "UPDATE [" + TABLE_NAME + "] SET " + RATING  + " = ?, " +  
														  WINS    + " = ?, " + 
														  LOSES   + " = ?, " + 
														  DRAWS   + " = ? WHERE " + NAME + " LIKE ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, user.getRating());
		statement.setInt(2, user.getWins());
		statement.setInt(3, user.getLoses());
		statement.setInt(4, user.getDraws());
		statement.setString(5, user.getName());
		return statement.executeUpdate() > 0;
	}
}
