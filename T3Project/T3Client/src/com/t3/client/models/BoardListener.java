package com.t3.client.models;

import com.t3.client.controllers.SessionCommunicate;
import com.t3.common.models.User;

/**
 * This interface using for listen what happen in board screen
 * @author Luan Vu
 *
 */
public interface BoardListener {
	public void onLeaveBoard(SessionCommunicate sessionComm, User user);
}
