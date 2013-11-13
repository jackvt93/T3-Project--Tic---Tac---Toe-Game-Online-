package com.t3.client.models;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.common.models.Board;
import com.t3.common.models.User;

/**
 * This interface using for listen what happen in room screen
 * @author Luan Vu
 *
 */
public interface RoomListener {
	/**
	 * Execute when user create board success
	 */
	public void onCreateBoardSuccess(SessionCommunicate sessionComm, User user, Board board);
	public void onJoinBoardSuccess(SessionCommunicate sessionComm, User user, Board board);
	public void onLogoutOK();
}
